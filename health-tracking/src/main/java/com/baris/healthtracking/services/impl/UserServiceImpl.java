package com.baris.healthtracking.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baris.healthtracking.dto.*;
import com.baris.healthtracking.entites.*;
import com.baris.healthtracking.exception.CustomException;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IUserService;

import jakarta.annotation.PostConstruct;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void checkRepository() {
        if (userRepository == null) {
            System.out.println("UserRepository yüklenemedi!");
        } else {
            System.out.println("UserRepository başarıyla yüklendi!");
        }
    }
    
    @Override
    public DtoUser createUser(DtoUserIU dtoUserIU) {
        User user = new User();
        BeanUtils.copyProperties(dtoUserIU, user);
        user.setPassword(passwordEncoder.encode(dtoUserIU.getPassword()));

        // Modüler veriler ekleniyor
        user.setWaterIntakes(mapToEntityList(dtoUserIU.getWaterIntakes(), user, WaterIntake.class));
        user.setCalorieIntakes(mapToEntityList(dtoUserIU.getCalorieIntakes(), user, CalorieIntake.class));
        user.setHeartRates(mapToEntityList(dtoUserIU.getHeartRates(), user, HeartRate.class));
        user.setHeightWeights(mapToEntityList(dtoUserIU.getHeightWeights(), user, HeightWeight.class));
        user.setSleeps(mapToEntityList(dtoUserIU.getSleeps(), user, Sleep.class));
        user.setStepCounts(mapToEntityList(dtoUserIU.getStepCounts(), user, StepCount.class));

        return mapToDto(userRepository.save(user));
    }

    @Override
    public List<DtoUser> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<DtoUser> dtoUsers = new ArrayList<>();

        for (User user : users) {
            dtoUsers.add(mapToDto(user));
        }

        return dtoUsers;
    }
    @Override
    public DtoUser getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        return mapToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public DtoUser updateUser(Long id, DtoUserIU dtoUserIU) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Kullanıcı bulunamadı.");
        }

        User user = optionalUser.get();

        // Şifre dışında tüm alanları güncelle
        user.setUserName(dtoUserIU.getUserName());
        user.setEmail(dtoUserIU.getEmail());
        user.setGender(dtoUserIU.getGender());

        // Kullanıcıyı kaydet ve DTO'ya dönüştür
        return mapToDto(userRepository.save(user));
    }


    @Override
    public void updateUserPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
    }

    @Override
    public void changeUserPassword(Long userId, DtoChangePassword dtoChangePassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new RuntimeException("User not found!");

        User user = optionalUser.get();
        if (!passwordEncoder.matches(dtoChangePassword.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }
        user.setPassword(passwordEncoder.encode(dtoChangePassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public DtoUser updateProfile(Long userId, DtoUpdateProfile dtoUpdateProfile) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new RuntimeException("User not found!");

        User user = optionalUser.get();
        user.setUserName(dtoUpdateProfile.getUserName());
        user.setEmail(dtoUpdateProfile.getEmail());
        user.setGender(dtoUpdateProfile.getGender());
        return mapToDto(userRepository.save(user));
    }

    @Override
    public DtoUser registerUser(DtoUserIU dtoUserIU) {
        if (userRepository.findByEmail(dtoUserIU.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        return createUser(dtoUserIU);
    }

    // Yardımcı metotlar
    private <T> List<T> mapToEntityList(List<?> dtos, User user, Class<T> entityType) {
        List<T> entities = new ArrayList<>();
        if (dtos != null) {
            dtos.forEach(dto -> {
                try {
                    T entity = entityType.getDeclaredConstructor().newInstance();
                    BeanUtils.copyProperties(dto, entity);
                    entityType.getMethod("setUser", User.class).invoke(entity, user);
                    entities.add(entity);
                } catch (Exception e) {
                    throw new RuntimeException("Mapping error: " + e.getMessage());
                }
            });
        }
        return entities;
    }

    public DtoUser mapToDto(User user) {
        DtoUser dtoUser = new DtoUser();
        BeanUtils.copyProperties(user, dtoUser);

        // Map modular entities
        if (user.getWaterIntakes() != null) {
            List<WaterIntakeOutputDto> waterDtos = new ArrayList<>();
            for (WaterIntake water : user.getWaterIntakes()) {
                WaterIntakeOutputDto waterDto = new WaterIntakeOutputDto();
                BeanUtils.copyProperties(water, waterDto);
                waterDtos.add(waterDto);
            }
            dtoUser.setWaterIntakes(waterDtos);
        }

        if (user.getCalorieIntakes() != null) {
            List<CalorieIntakeOutputDto> calorieDtos = new ArrayList<>();
            for (CalorieIntake calorie : user.getCalorieIntakes()) {
                CalorieIntakeOutputDto calorieDto = new CalorieIntakeOutputDto();
                BeanUtils.copyProperties(calorie, calorieDto);
                calorieDtos.add(calorieDto);
            }
            dtoUser.setCalorieIntakes(calorieDtos);
        }

        if (user.getHeartRates() != null) {
            List<HeartRateOutputDto> heartDtos = new ArrayList<>();
            for (HeartRate heartRate : user.getHeartRates()) {
                HeartRateOutputDto heartDto = new HeartRateOutputDto();
                BeanUtils.copyProperties(heartRate, heartDto);
                heartDtos.add(heartDto);
            }
            dtoUser.setHeartRates(heartDtos);
        }

        if (user.getHeightWeights() != null) {
            List<HeightWeightOutputDto> heightWeightDtos = new ArrayList<>();
            for (HeightWeight heightWeight : user.getHeightWeights()) {
                HeightWeightOutputDto heightWeightDto = new HeightWeightOutputDto();
                BeanUtils.copyProperties(heightWeight, heightWeightDto);
                heightWeightDtos.add(heightWeightDto);
            }
            dtoUser.setHeightWeights(heightWeightDtos);
        }

        if (user.getSleeps() != null) {
            List<SleepOutputDto> sleepDtos = new ArrayList<>();
            for (Sleep sleep : user.getSleeps()) {
                SleepOutputDto sleepDto = new SleepOutputDto();
                BeanUtils.copyProperties(sleep, sleepDto);
                sleepDtos.add(sleepDto);
            }
            dtoUser.setSleeps(sleepDtos);
        }

        if (user.getStepCounts() != null) {
            List<StepCountOutputDto> stepDtos = new ArrayList<>();
            for (StepCount step : user.getStepCounts()) {
                StepCountOutputDto stepDto = new StepCountOutputDto();
                BeanUtils.copyProperties(step, stepDto);
                stepDtos.add(stepDto);
            }
            dtoUser.setStepCounts(stepDtos);
        }

        return dtoUser;
    }

    
    @Override
    public boolean validateSession(String email) {
        // Veritabanında e-posta ile kullanıcıyı kontrol et
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent(); // Kullanıcı varsa true döner, yoksa false
    }
    
    

}
