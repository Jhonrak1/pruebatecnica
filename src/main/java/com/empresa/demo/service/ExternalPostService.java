package com.empresa.demo.service;

import com.empresa.demo.application.port.ExternalPostClient;
import com.empresa.demo.domain.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalPostService {

    private final ExternalPostClient externalPostClient;

    public ExternalPostService(ExternalPostClient externalPostClient) {
        this.externalPostClient = externalPostClient;
    }

    public List<PostDto> getPosts() {
        return externalPostClient.fetchPosts();
    }
}
