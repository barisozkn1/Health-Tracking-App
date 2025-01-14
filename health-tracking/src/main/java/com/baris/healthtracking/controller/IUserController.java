package com.baris.healthtracking.controller;

import java.util.List;

import com.baris.healthtracking.dto.DtoChangePassword;
import com.baris.healthtracking.dto.DtoUpdateProfile;
import com.baris.healthtracking.dto.DtoUser;
import com.baris.healthtracking.dto.DtoUserIU;

public interface IUserController {
	
	public DtoUser createUser(DtoUserIU dtoUserIU);
	
	public List<DtoUser> getAllUsers();
	
	public DtoUser getUserById(Long id);
	
	public void deleteUser(Long id);
	
	public DtoUser updateUser(Long id,DtoUserIU dtoUserIU);
	
	public void changePassword(Long userId, DtoChangePassword dtoChangePassword);
	
	public DtoUser updateProfile(Long userId, DtoUpdateProfile dtoUpdateProfile);


}
