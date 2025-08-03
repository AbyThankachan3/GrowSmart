package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.DTO.GeneralResponseDTO;
import com.GrowSmart.GrowSmart.DTO.LoginDTO;
import com.GrowSmart.GrowSmart.DTO.LoginResponseDTO;
import com.GrowSmart.GrowSmart.Entity.User;
import com.GrowSmart.GrowSmart.Entity.UserPrincipal;
import com.GrowSmart.GrowSmart.Repository.UserRepository;
import com.GrowSmart.GrowSmart.Util.CookieUtil;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoginResponseDTO loginUser(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //  Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        //  Fetch user details
        User userDetails = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + loginDTO.getEmail())
        );

        //  Generate tokens
        String accessToken = jwtService.generateAccessToken(userDetails.getEmail(), Collections.setOf(userDetails.getRole()));
        String refreshToken = jwtService.generateRefreshToken(userDetails.getEmail());

        // Store in HttpOnly cookies
        CookieUtil.setCookie(response, "jwt", accessToken, 86400);   // 15 min expiry
        CookieUtil.setCookie(response, "refresh", refreshToken, 604800); // 7 days expiry

        return new LoginResponseDTO("true","Login successful", userDetails.getRole().toString());
    }

    public GeneralResponseDTO logoutUser(String email, HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(response, "jwt");
        CookieUtil.deleteCookie(response, "refresh");
        return new GeneralResponseDTO("true", "Logged out successfully");
    }

    private UserDetails getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new)  // Convert StudentUser to UserPrincipa
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
