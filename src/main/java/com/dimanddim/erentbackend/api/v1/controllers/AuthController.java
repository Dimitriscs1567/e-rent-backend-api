package com.dimanddim.erentbackend.api.v1.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dimanddim.erentbackend.api.v1.entities.JwtTokensWhiteList;
import com.dimanddim.erentbackend.api.v1.repositories.JwtTokensWhiteListRepository;
import com.dimanddim.erentbackend.api.v1.repositories.UserRepository;
import com.dimanddim.erentbackend.api.v1.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokensWhiteListRepository tokensWhiteListRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody Map<String, String> data) {
        Map<Object, Object> response = new HashMap<>();

        try {
            String email = data.get("email");
            String password = data.get("password");
            if (email == null || password == null || password.equals("")) {
                response.put("message", "Request must contain an email and a password");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            if (!email.equals("mobile-app")) {
                deleteOtherTokens(email);
            }

            String token = jwtTokenProvider.createToken(email, this.userRepository.findByEmail(email).get().getRoles(),
                    false);
            tokensWhiteListRepository.save(new JwtTokensWhiteList(token));
            response.put("email", email);
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            response.put("message", "Invalid email/password supplied");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/signout")
    public ResponseEntity logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Map<Object, Object> model = new HashMap<>();

        if (token != null && jwtTokenProvider.validateToken(token)) {
            tokensWhiteListRepository.deleteById(token);
            model.put("message", "Signout completed.");
        } else {
            model.put("Error", "Invalid token.");
        }

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    void deleteOtherTokens(String email) {
        for (var token : tokensWhiteListRepository.findAll()) {
            if (!jwtTokenProvider.validateToken(token.getToken())
                    || jwtTokenProvider.getEmail(token.getToken()).equals(email)) {
                tokensWhiteListRepository.delete(token);
            }
        }
    }
}