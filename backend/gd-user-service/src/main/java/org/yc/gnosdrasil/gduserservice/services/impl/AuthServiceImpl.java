package org.yc.gnosdrasil.gduserservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yc.gnosdrasil.gduserservice.dtos.JwtResponseDTO;
import org.yc.gnosdrasil.gduserservice.dtos.LoginRequestDTO;
import org.yc.gnosdrasil.gduserservice.dtos.RegisterRequestDTO;
import org.yc.gnosdrasil.gduserservice.entities.User;
import org.yc.gnosdrasil.gduserservice.repository.UserRepository;
import org.yc.gnosdrasil.gduserservice.security.CustomUserDetails;
import org.yc.gnosdrasil.gduserservice.security.JwtUtils;
import org.yc.gnosdrasil.gduserservice.services.AuthService;
import org.yc.gnosdrasil.gduserservice.utils.mappers.UserMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public JwtResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);


        // Create UserDetailsImpl for authentication
        CustomUserDetails userDetails = CustomUserDetails.build(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String accessToken = jwtUtils.generateJwtToken(authentication);

        return new JwtResponseDTO(accessToken,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString());
    }

    @Override
    public JwtResponseDTO login(LoginRequestDTO loginRequest) {
        log.debug("Attempting to authenticate user: {}", loginRequest.username());

        try {
            // Ensure username is trimmed
            log.debug("Creating UsernamePasswordAuthenticationToken for user: {}", loginRequest.username());

            // Attempt to find the user directly first to check if they exist
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.username());

            if (userOpt.isEmpty()) {
                log.error("User not found for authentication check: {}", loginRequest.username());
                throw new RuntimeException("Invalid username or password");
            }

            User user = userOpt.get();

            // Check password match before trying authentication manager
            boolean passwordMatches = passwordEncoder.matches(loginRequest.password(), user.getPassword());
            log.debug("Pre-check: Password matches for user {}: {}", loginRequest.username(), passwordMatches);

            if (!passwordMatches) {
                log.error("Password mismatch for user: {}", loginRequest.username());
                throw new RuntimeException("Invalid username or password");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.username(), loginRequest.password());

            log.debug("Attempting authentication with AuthenticationManager");
            Authentication authentication = authenticationManager.authenticate(authToken);
            log.debug("Authentication successful, setting SecurityContext");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            log.debug("Retrieved UserDetailsImpl for user: {}", userDetails.getUsername());

            String jwt = jwtUtils.generateJwtToken(authentication);
            log.debug("Generated JWT token successfully");

            return new JwtResponseDTO(jwt,
                    "Bearer",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().toString());
        } catch (Exception e) {
            log.error("Authentication failed for user: {} - Exception: {} - Stack trace: {}",
                    loginRequest.username(), e.getMessage(), e.getStackTrace());
            if (e instanceof BadCredentialsException) {
                log.error("Bad credentials for user: {}", loginRequest.username());
            } else if (e instanceof UsernameNotFoundException) {
                log.error("Username not found: {}", loginRequest.username());
            }
            throw new RuntimeException("Invalid username or password");
        }
    }

//    @Override
//    public void logout(String refreshToken) {
//        final String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
//
//        if (username == null) {
//            throw new ValidationException("Invalid refresh token");
//        }
//
//        User user = userService.findByUsername(username)
//                .orElseThrow(() -> new ValidationException("User not found"));
//
//        if (!jwtUtils.isTokenValid(refreshToken)) {
//            throw new ValidationException("Invalid refresh token");
//        }
//    }
}