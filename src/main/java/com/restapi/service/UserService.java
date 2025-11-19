package com.restapi.service;

import com.restapi.dto.AuthDto;
import com.restapi.dto.AuthDto;
import com.restapi.exception.common.AppException;
import com.restapi.exception.common.InvalidUserException;
import com.restapi.model.AppUser;
import com.restapi.model.Role;
import com.restapi.repository.RoleRepository;
import com.restapi.repository.UserRepository;
import com.restapi.request.LoginRequest;
import com.restapi.request.RegisterRequest;
import com.restapi.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private AuthDto authDto;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthResponse register(RegisterRequest registerRequest) {
            AppUser appUser = authDto.mapToAppUser(registerRequest);
            appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
            
            // Assign role based on request: "vendor" -> ADMIN, "user" or null -> USER
            String requestedRole = registerRequest.getRole();
            if (requestedRole != null && requestedRole.equalsIgnoreCase("vendor")) {
                appUser.setRoles(roleRepository.findByName(Role.ADMIN));
            } else {
                appUser.setRoles(roleRepository.findByName(Role.USER));
            }
            
            try{
                appUser = userRepository.save(appUser);
            }
            catch (DataIntegrityViolationException e){
                // Check if it's a unique constraint violation (duplicate username/email)
                if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                    throw new InvalidUserException("User Already Exists");
                }
                // Otherwise, it's a constraint violation (missing required fields)
                String errorMsg = e.getCause() != null && e.getCause().getMessage() != null 
                    ? e.getCause().getMessage() 
                    : "Missing required fields";
                throw new AppException(HttpStatus.BAD_REQUEST, "Registration failed: " + errorMsg);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                throw new AppException(HttpStatus.BAD_REQUEST, "Registration failed: " + e.getMessage());
            }
            return authDto.mapToAuthResponse(appUser);
    }

    public AuthResponse login(LoginRequest loginRequest) {

        AppUser appUser = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidUserException("Invalid user credentials"));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), appUser.getPassword())) {
            throw new InvalidUserException("Invalid password");
        }

        return authDto.mapToAuthResponse(appUser);
    }

    public List<AppUser> getAllProfiles() {
        return userRepository.findAll();
    }

    public List<AppUser> deleteUser(long id) {
        userRepository.deleteById(id);
        return userRepository.findAll();
    }
}
