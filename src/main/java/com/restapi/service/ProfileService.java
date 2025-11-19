package com.restapi.service;

import com.restapi.exception.common.ResourceNotFoundException;
import com.restapi.model.AppUser;
import com.restapi.model.UserDP;
import com.restapi.repository.DpRepository;
import com.restapi.repository.UserRepository;
import com.restapi.request.ProfileRequest;
import com.restapi.request.RegisterRequest;
import com.restapi.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DpRepository dpRepository;

    public AppUser findUser(Long id) {
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException(String.valueOf(id)));
    }

    public AppUser updateUser(ProfileRequest profileRequest) {
        AppUser appUser=new AppUser();
        appUser.setName(profileRequest.getName());
        appUser.setUsername(profileRequest.getUsername());
        appUser.setEmail(profileRequest.getEmail());
        appUser.setAddress(profileRequest.getAddress());
        appUser.setPhone(profileRequest.getPhone());
        appUser.setGender(profileRequest.getGender());
        appUser.setPassword(userRepository.findById(profileRequest.getId()).get().getPassword());
        appUser.setId(profileRequest.getId());
        appUser.setRoles(userRepository.findById(profileRequest.getId()).get().getRoles());
        appUser.setOrder(userRepository.findById(profileRequest.getId()).get().getOrder());
        userRepository.save(appUser);
        return findUser(profileRequest.getId());
    }

    public UserDP addDp(UserDP dp) {
        return dpRepository.save(dp);
    }

    public UserDP findDp(Long id) {
        return dpRepository.findByUserId(id);
    }
}
