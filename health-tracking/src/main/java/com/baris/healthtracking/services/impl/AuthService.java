package com.baris.healthtracking.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baris.healthtracking.dto.DtoUser;
import com.baris.healthtracking.dto.DtoUserIU;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DtoUser registerUser(DtoUserIU dtoUserIU) {
        // Email adresinin benzersiz olup olmadığını kontrol et
        if (userRepository.findByEmail(dtoUserIU.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + dtoUserIU.getEmail());
        }

        // Yeni kullanıcı oluşturma
        DtoUser response = new DtoUser();
        User user = new User();
        BeanUtils.copyProperties(dtoUserIU, user);

        // Şifreyi şifreleme
        user.setPassword(passwordEncoder.encode(dtoUserIU.getPassword()));

        // Kullanıcıyı veritabanına kaydet
        User dbUser = userRepository.save(user);
        BeanUtils.copyProperties(dbUser, response);

        return response;
    }
}
