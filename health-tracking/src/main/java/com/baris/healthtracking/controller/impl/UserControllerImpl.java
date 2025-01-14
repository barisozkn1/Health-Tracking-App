package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.controller.IUserController;
import com.baris.healthtracking.dto.*;
import com.baris.healthtracking.services.IUserService;
import com.baris.healthtracking.services.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/api/user")
public class UserControllerImpl implements IUserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(path = "/create")
    @Override
    public DtoUser createUser(@RequestBody @Valid DtoUserIU dtoUserIU) {
        return userService.createUser(dtoUserIU);
    }

    @GetMapping(path = "/list")
    @Override
    public List<DtoUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public DtoUser getUserById(@PathVariable(name = "id") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public DtoUser updateUser(@PathVariable(name = "id") Long id, @RequestBody DtoUserIU dtoUserIU) {
        return userService.updateUser(id, dtoUserIU);
    }

    @PostMapping(path = "/change-password/{id}")
    @Override
    public void changePassword(@PathVariable Long id, @RequestBody @Valid DtoChangePassword dtoChangePassword) {
        userService.changeUserPassword(id, dtoChangePassword);
    }

    @PutMapping(path = "/update-profile/{id}")
    @Override
    public DtoUser updateProfile(@PathVariable Long id, @RequestBody @Valid DtoUpdateProfile dtoUpdateProfile) {
        return userService.updateProfile(id, dtoUpdateProfile);
    }
}
