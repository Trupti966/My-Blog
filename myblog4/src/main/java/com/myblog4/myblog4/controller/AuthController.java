package com.myblog4.myblog4.controller;

import com.myblog4.myblog4.entity.Roles;
import com.myblog4.myblog4.entity.User;
import com.myblog4.myblog4.payload.JWTAuthResponse;
import com.myblog4.myblog4.payload.LoginDto;
import com.myblog4.myblog4.payload.SignUpDto;
import com.myblog4.myblog4.repository.RolesRepository;
import com.myblog4.myblog4.repository.UserRepository;
import com.myblog4.myblog4.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesRepository rolesRepository;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenProvider tokenProvider;

    // http://localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }




    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

        // Check if the email or username already exists
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already in use - " + signUpDto.getEmail(), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already in use - " + signUpDto.getUsername(), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // By default, for all signups, it will assign the ROLE_ADMIN to the user
        Roles userRole = rolesRepository.findByName("ROLE_ADMIN").orElse(null);
        if (userRole == null) {
            return new ResponseEntity<>("Role not found!", HttpStatus.NOT_FOUND);
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!!", HttpStatus.CREATED);
    }


}



