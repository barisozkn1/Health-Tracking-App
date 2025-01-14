package com.baris.healthtracking.services;

import java.util.List;

import com.baris.healthtracking.dto.DtoChangePassword;
import com.baris.healthtracking.dto.DtoResetPassword;
import com.baris.healthtracking.dto.DtoUpdateProfile;
import com.baris.healthtracking.dto.DtoUser;
import com.baris.healthtracking.dto.DtoUserIU;
import com.baris.healthtracking.entites.User;

public interface IUserService {

    DtoUser createUser(DtoUserIU dtoUserIU);

    List<DtoUser> getAllUsers();

    DtoUser getUserById(Long id);

    void deleteUser(Long id);

    DtoUser updateUser(Long id, DtoUserIU dtoUserIU);

    void updateUserPasswords();

    void changeUserPassword(Long userId, DtoChangePassword dtoChangePassword);

    DtoUser updateProfile(Long userId, DtoUpdateProfile dtoUpdateProfile);

    DtoUser registerUser(DtoUserIU dtoUserIU);

    boolean validateSession(String email);

    DtoUser mapToDto(User user); // Yeni metod tanımı

}
