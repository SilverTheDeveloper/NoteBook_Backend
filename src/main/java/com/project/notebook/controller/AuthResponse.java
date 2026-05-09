package com.project.notebook.controller;

/**
 * AuthResponse — returned by both /register and /login
 * { "token": "...", "id": 1, "name": "...", "email": "..." }
 *
 * Password is intentionally NOT included here.
 */
public class AuthResponse {
    private String token;
    private Long   id;
    private String name;
    private String email;

    public AuthResponse(String token, Long id, String name, String email) {
        this.token = token;
        this.id    = id;
        this.name  = name;
        this.email = email;
    }

    public String getToken() { return token; }
    public Long   getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
}
