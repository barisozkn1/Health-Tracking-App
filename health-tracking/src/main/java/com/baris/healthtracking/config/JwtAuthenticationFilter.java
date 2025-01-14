package com.baris.healthtracking.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baris.healthtracking.entites.TokenBlacklist;
import com.baris.healthtracking.services.impl.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklist tokenBlacklist; // Kara liste servisini ekliyoruz

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Şifre sıfırlama endpointleri için token kontrolünü atla
            String uri = request.getRequestURI();
            if (uri.contains("/rest/api/psword/send-reset-token") || uri.contains("/rest/api/psword/reset-password")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Request'ten token al
            String token = getJwtFromRequest(request);
            System.out.println("Header'dan alınan token: " + token);

            // Token kara listede mi kontrol et
            if (token != null && tokenBlacklist.contains(token)) {
                System.out.println("Token kara listede, erişim reddedildi.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Yetkisiz erişim yanıtı
                response.getWriter().write("Token kara listede, lutfen tekrar giris yapin.");
                return; // İşlemi sonlandır
            }

            // Token doğrulaması yapılır
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // Token'dan kullanıcı adı çıkarılır
                String username = jwtTokenProvider.getUsernameFromToken(token);
                System.out.println("Doğrulanan kullanıcı adı: " + username);

                // Kullanıcı detaylarını yükle
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    // Kullanıcı doğrulama nesnesi oluşturulur
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Authentication, SecurityContextHolder'a atanır
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Kullanıcı doğrulandı ve SecurityContext'e atandı.");
                }
            }
        } catch (Exception e) {
            System.out.println("Kimlik doğrulama sırasında bir hata oluştu: " + e.getMessage());
        }

        // Filtre zincirine devam et
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        System.out.println("Bearer Token bulunamadı veya geçersiz.");
        return null;
    }
}
