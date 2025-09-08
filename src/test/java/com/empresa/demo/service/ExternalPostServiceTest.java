package com.empresa.demo.service;

import com.empresa.demo.application.port.ExternalClient;
import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.domain.exception.ClientErrorException;
import com.empresa.demo.domain.exception.ServerErrorException;
import com.empresa.demo.domain.exception.TimeoutException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPostServiceTest {

    @Mock
    private ExternalClient client;

    @InjectMocks
    private ExternalPostService service;

    @Test
    void givenClientError_thenInvalidRequestSignal() {
        when(client.fetchPosts()).thenThrow(new ClientErrorException("Bad request"));

        List<PostDto> result = service.getPosts();

        assertEquals(1, result.size());
        assertEquals("INVALID_REQUEST", result.get(0).getTitle());
    }

    @Test
    void givenServerError_thenRetryAndFail_thenFallback() {
        when(client.fetchPosts()).thenThrow(new ServerErrorException("Server error"));

        List<PostDto> result = service.getPosts();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void givenTimeout_thenFallbackEmptyList() {
        when(client.fetchPosts()).thenThrow(new TimeoutException("Timeout", null));

        List<PostDto> result = service.getPosts();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void givenSuccess_thenReturnPosts() {
        List<PostDto> posts = List.of(new PostDto(1, 1, "title", "body"));
        when(client.fetchPosts()).thenReturn(posts);

        List<PostDto> result = service.getPosts();

        assertEquals(posts, result);
    }
}