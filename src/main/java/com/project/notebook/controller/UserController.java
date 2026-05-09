package com.project.notebook.controller;

import com.project.notebook.config.JwtUtil;
import com.project.notebook.entity.User;
import com.project.notebook.repository.RepoUser;
import com.project.notebook.services.user.UserServicesMain;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController — traditional Spring Security login flow.
 *
 * POST /api/users/register  → hashes password via BCrypt, saves user, returns JWT
 * POST /api/users/login     → delegates to AuthenticationManager which calls
 *                             DaoAuthenticationProvider → CustomUserDetailsService
 *                             → BCrypt comparison — all done by Spring Security
 * GET  /api/users/          → list all (protected)
 * GET  /api/users/{id}      → get by id (protected)
 * DELETE /api/users/{id}    → delete (protected)
 *
 * Passwords are NEVER returned in any response.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServicesMain     serv;
    private final JwtUtil              jwtUtil;
    private final AuthenticationManager authManager;
    private final RepoUser             userRepo;

    public UserController(UserServicesMain serv,
                          JwtUtil jwtUtil,
                          AuthenticationManager authManager,
                          RepoUser userRepo) {
        this.serv        = serv;
        this.jwtUtil     = jwtUtil;
        this.authManager = authManager;
        this.userRepo    = userRepo;
    }

    /** Register — BCrypt hashes the password in UserServicesImpl, then returns a JWT. */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) {
        User saved = serv.registerUser(user);
        String token = jwtUtil.generateToken(saved.getId());
        return ResponseEntity.ok(
            new AuthResponse(token, saved.getId(), saved.getName(), saved.getEmail())
        );
    }

    /**
     * Login — the traditional Spring Security way.
     *
     * Step 1: We wrap the email + password in a UsernamePasswordAuthenticationToken
     *         (this is Spring Security's standard unauthenticated token object)
     *
     * Step 2: We hand it to AuthenticationManager.authenticate()
     *         Spring Security then internally:
     *           → calls DaoAuthenticationProvider
     *           → calls CustomUserDetailsService.loadUserByUsername(email)
     *           → loads the User from DB
     *           → uses BCryptPasswordEncoder.matches(rawPassword, hashedPassword)
     *           → throws BadCredentialsException if wrong
     *           → returns a fully authenticated Authentication object if correct
     *
     * Step 3: We get the email from the authenticated object, look up the full
     *         User entity (to get id/name), generate a JWT and return it.
     *
     * This is the correct, idiomatic Spring Security login pattern.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest req) {
        try {
            // Step 1 + 2 — Spring Security does the full authentication
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.getEmail(),    // principal
                    req.getPassword()  // credentials
                )
            );

            // Step 3 — authentication.getName() returns the email (our "username")
            String email = authentication.getName();
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user.getId());
            return ResponseEntity.ok(
                new AuthResponse(token, user.getId(), user.getName(), user.getEmail())
            );

        } catch (AuthenticationException e) {
            // Spring Security throws this for bad credentials — return 401
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    @GetMapping("/")
    public List<User> getAllUser() {
        return serv.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return serv.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        serv.deleteUserById(id);
    }
}
