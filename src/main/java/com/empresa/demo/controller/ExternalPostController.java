package com.empresa.demo.controller;

import com.empresa.demo.application.port.ExternalPostClient;
import com.empresa.demo.domain.dto.PostDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExternalPostController {

    private final ExternalPostClient externalPostClient;

    public ExternalPostController(ExternalPostClient externalPostClient) {
        this.externalPostClient = externalPostClient;
    }

    @GetMapping("/external/posts")
    public List<PostDto> getExternalPosts() {
        return externalPostClient.fetchPosts();
    }
}
