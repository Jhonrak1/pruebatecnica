package com.empresa.demo.service;

import com.empresa.demo.application.port.ExternalClient;
import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.domain.exception.ClientErrorException;
import com.empresa.demo.domain.exception.ServerErrorException;
import com.empresa.demo.domain.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ExternalPostService {

    private static final Logger log = LoggerFactory.getLogger(ExternalPostService.class);

    private final ExternalClient client;

    public ExternalPostService(ExternalClient client) {
        this.client = client;
    }

    public List<PostDto> getPosts() {
        try {
            return client.fetchPosts();
        } catch (ClientErrorException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return List.of(new PostDto(null, null, "INVALID_REQUEST", null));
        } catch (ServerErrorException e) {
            log.error("Server error after retries exhausted: {}", e.getMessage());
            return Collections.emptyList();
        } catch (TimeoutException e) {
            log.error("Timeout: {}", e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
