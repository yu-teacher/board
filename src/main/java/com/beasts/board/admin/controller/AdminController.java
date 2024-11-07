package com.beasts.board.admin.controller;

import com.beasts.board.admin.dto.AdminLoginRequestDTO;
import com.beasts.board.admin.dto.AdminLoginResponseDTO;
import com.beasts.board.admin.entity.Admin;
import com.beasts.board.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDTO> login(@RequestBody @Valid AdminLoginRequestDTO request) {
        Admin admin = adminService.login(request.getUsername(), request.getPassword());
        String token = adminService.createToken(admin);
        return ResponseEntity.ok(new AdminLoginResponseDTO(token));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        adminService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        adminService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
