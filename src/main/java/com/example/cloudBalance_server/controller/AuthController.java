package com.example.cloudBalance_server.controller;

import com.example.cloudBalance_server.dto.JwtResponse;
import com.example.cloudBalance_server.dto.LoginRequest;
import com.example.cloudBalance_server.security.CustomUserDetailsService;
import com.example.cloudBalance_server.security.JwtUtils;
import com.example.cloudBalance_server.service.TokenBlacklistService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService blacklistService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest)
        {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);


            // 2. Get User Details
            UserDetails user = (UserDetails) auth.getPrincipal();

            // 3. Generate Tokens
                String accessToken = jwtUtils.generateAccessToken(user);
                String refreshToken = jwtUtils.generateRefreshToken(user);



            // 4. Cookie ko ResponseEntity ke header mein hi chipka dena
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .build();

            // 5. Final Response: Body mein Access Token + Header mein Cookie
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new JwtResponse(accessToken));
        }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
         // null check
        // try -catch
        //try : step1  = username from refreshToken , step2 = db se username ( loaduserbyusername )
        // step3 : validate ( token string , user )
        // step4 : access token generate

        // null check
        if(refreshToken == null){
            return ResponseEntity.status(404).body("refresh token is msiing");
        }
        try {
            // 1. Token kholo aur naam pata karo
            String username = jwtUtils.extractUsername(refreshToken);

            // 2. Database se user ki jankari nikaalo
            UserDetails user = userDetailsService.loadUserByUsername(username);

            // 3. Check karo: Kya token sahi hai? (Invalid hone par seedha error return)
            if (!jwtUtils.validateToken(refreshToken, user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mismatch or tempered");
            }

            // 4. Sab sahi hai! Naya Access Token banao
            String newAccessToken = jwtUtils.generateAccessToken(user);

            // 5. User ko naya token thama do
            return ResponseEntity.ok(new JwtResponse(newAccessToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired ya galat hai");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Date expiration = jwtUtils.extractExpiration(token);
                long diff = expiration.getTime() - System.currentTimeMillis();
                if (diff > 0) {
                    blacklistService.blacklistToken(token, diff);
                }
            } catch (Exception e) {
              //
            }
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }
}
