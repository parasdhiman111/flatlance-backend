package com.example.grocerymono.controllers;

import com.example.grocerymono.dto.requests.LoginRequest;
import com.example.grocerymono.dto.requests.SignUpRequest;
import com.example.grocerymono.dto.responses.JwtResponse;
import com.example.grocerymono.dto.responses.MessageResponse;
import com.example.grocerymono.models.PasswordConfirmationToken;
import com.example.grocerymono.models.enums.Gender;
import com.example.grocerymono.models.Role;
import com.example.grocerymono.models.enums.RoleEnum;
import com.example.grocerymono.models.User;
import com.example.grocerymono.repositories.PasswordConfirmationTokenRepository;
import com.example.grocerymono.repositories.RoleRepository;
import com.example.grocerymono.repositories.UserRepository;
import com.example.grocerymono.security.jwt.JwtUtils;
import com.example.grocerymono.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${paras.app.serviceUrl}")
    private String serviceUrl;

    @PostMapping("/login")
    public ResponseEntity<?> loginRequest(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateToken(authentication);
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
           userDetails.getUserId(),
           userDetails.getUsername(),
                roles

        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpRequest(@RequestBody SignUpRequest signUpRequest)
    {
        System.out.println("----------"+signUpRequest.getEmail());
        if(userRepository.existsByEmail(signUpRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already exists"));
        }
        User newUser=new User();
        newUser.setFirstName(signUpRequest.getFirstName());
        newUser.setLastName(signUpRequest.getLastName());
        newUser.setEmail(signUpRequest.getEmail());
        if(signUpRequest.getGender().equalsIgnoreCase("male"))
            newUser.setGender(Gender.MALE);
        else if(signUpRequest.getGender().equalsIgnoreCase("female"))
            newUser.setGender(Gender.FEMALE);
        else
            newUser.setGender(Gender.MALE);
        newUser.setContactNumber(signUpRequest.getContactNumber());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "owner":
                        Role ownerRole = roleRepository.findByName(RoleEnum.ROLE_OWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(ownerRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        newUser.setRoles(roles);
        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @PostMapping("/forgot")
    public ResponseEntity<?> forgetPassword(@RequestParam String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
        {
            PasswordConfirmationToken confirmationToken=new PasswordConfirmationToken(user.get());
            confirmationTokenRepository.save(confirmationToken);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Password Reset Mail");
            mailMessage.setText("To complete the password reset process, please click here: "
                    +serviceUrl+"/api/auth/confirm-reset?token="+confirmationToken.getConfirmationToken());
                mailSender.send(mailMessage);

            return ResponseEntity.ok().body("Successfully sent");
        }
        return ResponseEntity.ok().body("User is not present with email :"+email);
    }


}
