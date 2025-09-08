package com.empresa.demo.controller;

import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.service.ExternalPostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(controllers = ExternalPostController.class)
class ExternalPostControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ExternalPostService service;

    @Test
    void givenServiceResponse_whenCallEndpoint_thenReturnPosts() {
        PostDto dto = new PostDto(1, 1, "", "");
        Mockito.when(service.getPosts()).thenReturn(List.of(dto));

        webTestClient.get().uri("/external/posts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PostDto.class)
                .hasSize(1);
    }
}