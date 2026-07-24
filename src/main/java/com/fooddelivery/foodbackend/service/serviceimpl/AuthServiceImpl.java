package com.fooddelivery.foodbackend.service.serviceimpl;

import com.fooddelivery.foodbackend.dto.request.LoginRequest;
import com.fooddelivery.foodbackend.dto.response.LoginResponse;
import com.fooddelivery.foodbackend.dto.request.RegisterRequest;
import com.fooddelivery.foodbackend.dto.response.UserResponse;
import com.fooddelivery.foodbackend.entity.AppRole;
import com.fooddelivery.foodbackend.entity.Role;
import com.fooddelivery.foodbackend.entity.User;
import com.fooddelivery.foodbackend.exception.BadRequestException;
import com.fooddelivery.foodbackend.exception.ResourceNotFoundException;
import com.fooddelivery.foodbackend.repository.RoleRepository;
import com.fooddelivery.foodbackend.repository.UserRepository;
import com.fooddelivery.foodbackend.security.CustomUserDetails;
import com.fooddelivery.foodbackend.security.JwtService;
import com.fooddelivery.foodbackend.service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final ModelMapper modelMapper;
    @Override
    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists");
        }

        User user = modelMapper.map(request,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role =roleRepository.findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);
        User  savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserResponse.class);
    }


    @Override
    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(
                new CustomUserDetails(user)
        );
        return new LoginResponse(token,"Bearer");
    }

    @Override
    public UserResponse getCurrentUser() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user,UserResponse.class);
    }


}