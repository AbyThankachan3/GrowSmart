package com.GrowSmart.GrowSmart.Controller;

import com.GrowSmart.GrowSmart.DTO.GeneralResponseDTO;
import com.GrowSmart.GrowSmart.DTO.LoginDTO;
import com.GrowSmart.GrowSmart.DTO.LoginResponseDTO;
import com.GrowSmart.GrowSmart.DTO.UserDTO;
import com.GrowSmart.GrowSmart.Service.AuthService;
import com.GrowSmart.GrowSmart.Service.JWTService;
import com.GrowSmart.GrowSmart.Service.UserService;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/auth")
public class AuthController {
    private final JWTService jwtService;
    private final AuthService authService;
    private final UserService userService;

    public AuthController(JWTService jwtService, AuthService authService, UserService userService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO)  {
        boolean created = userService.createUser(userDTO);
        GeneralResponseDTO response = new GeneralResponseDTO();
        response.setMessage(created ? "Student registered successfully" : "Student already exists");
        response.setSuccess(Boolean.toString(created));
        return new ResponseEntity<>(response, created ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @Operation(summary = "User Login", description = "Authenticates a user and returns JWT in HttpOnly cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ResponseEntity<>(authService.loginUser(loginDTO, request, response), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "User Logout", description = "Clears the JWT and refresh token cookies to log out the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<GeneralResponseDTO> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = authentication.getName();
        return ResponseEntity.ok(authService.logoutUser(email, request, response));
    }
}