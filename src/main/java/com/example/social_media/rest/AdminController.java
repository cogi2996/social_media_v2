package com.example.social_media.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
// class level security
// @PreAuthorize("hasRole('ADMIN')") // hasRole('ADMIN') == hasAuthority('ROLE_ADMIN')
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping
    public String get() {
        return "Get : admin controller";
    }

    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping

    public String post() {
        return "Post : admin controller";
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping
    public String put() {
        return "put : admin controller";
    }

    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping
    public String delete() {
        return "put : admin controller";
    }
}
