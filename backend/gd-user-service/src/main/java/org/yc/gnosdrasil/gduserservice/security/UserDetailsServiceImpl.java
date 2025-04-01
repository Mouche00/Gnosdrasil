package org.yc.gnosdrasil.gduserservice.security;

import org.yc.gnosdrasil.gduserservice.entities.User;
import org.yc.gnosdrasil.gduserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        // Ensure username is trimmed to remove whitespace issues
        final String trimmedUsername = username.trim();
        log.debug("Trimmed username for lookup: '{}' (length: {})", trimmedUsername, trimmedUsername.length());

        // First try exact match
        Optional<User> userOpt = userRepository.findByUsername(trimmedUsername);

        if (userOpt.isEmpty()) {
            log.error("User still not found after case-insensitive search");
            throw new UsernameNotFoundException("User Not Found with username: " + trimmedUsername);
        } else {
            log.info("User found with case-insensitive search: {} (original input: {})",
                    userOpt.get().getUsername(), trimmedUsername);
        }

        User user = userOpt.get();


        UserDetails userDetails = CustomUserDetails.build(user);
        log.debug("Built UserDetailsImpl for user: {}", user.getUsername());

        return userDetails;
    }
}
