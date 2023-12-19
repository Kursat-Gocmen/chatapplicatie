package com.backend.chatapplicatie.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Tag(name = "Authorization")
@RequestMapping("/api/board")
public class BoardController {

    @GetMapping("/user")
    public String userAccess() {
        return "";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "";
    }
}
