package com.example.grocerymono.controllers;

import com.example.grocerymono.models.PasswordConfirmationToken;
import com.example.grocerymono.models.User;
import com.example.grocerymono.repositories.PasswordConfirmationTokenRepository;
import com.example.grocerymono.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@CrossOrigin(origins = "*",maxAge = 3600)
@Controller
@RequestMapping("/api/auth")
public class ResetPasswordController {

    @Autowired
    private PasswordConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView resetPasswordNotification(ModelAndView modelAndView,@RequestParam("token")String confirmationToken)
    {

        PasswordConfirmationToken token =
                confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token!=null)
        {
            Optional<User> user = userRepository.findByEmail(token.getUser().getEmail());
            modelAndView.addObject("user", user);
            modelAndView.addObject("emailId", token.getUser().getEmail());
            modelAndView.setViewName("resetPassword");
            confirmationTokenRepository.delete(token);
        }
        else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(ModelAndView modelAndView, User user)
    {
        if(user.getEmail()!=null)
        {
            Optional<User> tokenUser=userRepository.findByEmail(user.getEmail());
            if(tokenUser.isPresent())
            {
                tokenUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(tokenUser.get());
                modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");

                modelAndView.setViewName("successPasswordReset");
            }

        }
        else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;

    }

}
