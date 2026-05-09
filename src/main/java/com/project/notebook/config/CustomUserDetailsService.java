package com.project.notebook.config;

import com.project.notebook.entity.User;
import com.project.notebook.repository.RepoUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService — implements Spring Security's UserDetailsService.
 *
 * Spring Security calls loadUserByUsername() automatically during authentication.
 * It uses the returned UserDetails object to:
 *   1. Get the stored (hashed) password for comparison
 *   2. Get the user's roles/authorities for authorization
 *   3. Check if the account is enabled, locked, expired etc.
 *
 * We use email as the "username" since that's what users log in with.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RepoUser userRepo;

    public CustomUserDetailsService(RepoUser userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Called by Spring Security with the email the user typed.
     * Must return a UserDetails object or throw UsernameNotFoundException.
     *
     * Spring Security then handles the BCrypt password comparison internally.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user found with email: " + email));

        /*
         * We return Spring Security's built-in User (not our entity User).
         * It implements UserDetails and gives Spring Security:
         *   - username  (we use email)
         *   - password  (BCrypt hash from DB — Spring compares this)
         *   - authorities (roles — empty list since we have no roles yet)
         *
         * The 3 booleans at the end are:
         *   enabled, accountNonExpired, credentialsNonExpired, accountNonLocked
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())        // BCrypt hash
                .authorities("ROLE_USER")            // basic role — required, can't be empty
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
