package com.empresa.demo.controller;

import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.service.ExternalPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExternalPostController {

    private final ExternalPostService service;

    public ExternalPostController(ExternalPostService service) {
        this.service = service;
    }

    @GetMapping("/external/posts")
    public List<PostDto> getPosts() {
        return service.getPosts();
    }
}
