package com.example.polls.controllers;


import com.example.polls.exception.AppException;
import com.example.polls.models.RoleName;
import com.example.polls.models.Roles;
import com.example.polls.models.User;
import com.example.polls.payload.ApiResponse;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.repositories.RoleRepository;
import com.example.polls.repositories.UserRepository;
import com.example.polls.security.JwtTokenProvider;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticationManager(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOremail(),
                        loginRequest.getPassword()
                )
        );

        /*The SecurityContext and SecurityContextHolder are two fundamental classes
        of Spring Security. The SecurityContext is used to store the details of the currently authenticated user,
        also known as a principle. So, if you have to get the username or any other user details, you need to get this SecurityContext first.
         The SecurityContextHolder is a helper class, which provide access to the security context. By default, it uses a ThreadLocal object to store security context,
         which means that the security context is always available to methods in the same thread of execution, even if you don't pass the SecurityContext object around.
         Don't worry about the ThreadLocal memory leak in web application though, Spring Security takes care of cleaning ThreadLocal.
          Read more: https://javarevisited.blogspot.com/2018/02/what-is-securitycontext-and-SecurityContextHolder-Spring-security.html#ixzz5IoTpBosU*/
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // se consigue el usuario authentificado para luego tener sus datos y crear un token con su id para leugo generarlo y psaarlo en el header
        String jwt  = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));// aqui usamos el payload que creamos para enviar la repsuesta con el jwt
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){

        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }



        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),// se crea un objeto User con el Objeto tipo SignUp
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));// encripta la contrasena

        roleRepository.save(new Roles());

        Roles userRole = roleRepository.findByName(RoleName.ROLE_USER)// se busca en el repositorio el rol User
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));// se le pone ese rol a el usuario

        User result = userRepository.save(user);//se guarda el usuario

        URI location = ServletUriComponentsBuilder// se manda un uri para llevar al usuario al link donde se creo
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}


















