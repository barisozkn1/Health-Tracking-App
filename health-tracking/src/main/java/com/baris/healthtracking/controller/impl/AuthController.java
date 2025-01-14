package com.baris.healthtracking.controller.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baris.healthtracking.config.JwtTokenProvider;
import com.baris.healthtracking.dto.DtoUser;
import com.baris.healthtracking.dto.DtoUserIU;
import com.baris.healthtracking.dto.LoginRequest;
import com.baris.healthtracking.dto.LoginResponse;
import com.baris.healthtracking.entites.TokenBlacklist;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IUserService;
import com.baris.healthtracking.services.impl.CustomUserDetailsService;
import com.baris.healthtracking.services.impl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Giriş yapmaya çalışan email: " + loginRequest.getEmail());
            System.out.println("Giriş yapmaya çalışan şifre: " + loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kullanıcıya JWT token oluştur
            String jwt = tokenProvider.generateToken(authentication);
            System.out.println("Oluşturulan JWT Token: " + jwt);

            // Veritabanından kullanıcı detaylarını al
            User authenticatedUser = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

            System.out.println("Kullanıcı Adı (userName): " + authenticatedUser.getUserName());
            System.out.println("Kullanıcı ID: " + authenticatedUser.getId());

            // JWT, kullanıcı adı ve userId ile LoginResponse döndürülür
            return new LoginResponse(jwt, authenticatedUser.getUserName(), authenticatedUser.getId());
        } catch (Exception e) {
            System.out.println("Giriş hatası: " + e.getMessage());
            throw new RuntimeException("Giriş başarısız! Kullanıcı adı veya şifre yanlış.", e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String token = getJwtFromRequest(request);

        if (token != null && tokenProvider.validateToken(token)) {
            tokenBlacklist.add(token); // Tokeni kara listeye ekle
            return ResponseEntity.ok("Kullanıcı başarıyla çıkış yaptı.");
        }

        return ResponseEntity.badRequest().body("Token geçersiz veya bulunamadı.");
    }

    @PostMapping("/register")
    public DtoUser registerUser(@RequestBody @Valid DtoUserIU dtoUserIU) {
        return userService.registerUser(dtoUserIU); // UserService'deki registerUser çağrılıyor
    }

    @GetMapping("/validate-session")
    public ResponseEntity<?> validateSession(HttpServletRequest request) {
        String token = getJwtFromRequest(request);

        if (token != null && tokenProvider.validateToken(token)) {
            // JWT'den e-posta alınır
            String email = tokenProvider.getUsernameFromToken(token);

            // Kullanıcının oturumu geçerli mi kontrol edilir
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                // Kullanıcı bilgilerini DTO olarak döndür
                DtoUser dtoUser = userService.mapToDto(optionalUser.get());
                return ResponseEntity.ok(dtoUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Oturum geçersiz.");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token geçersiz veya bulunamadı.");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
