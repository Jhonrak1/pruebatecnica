package com.empresa.demo.application.port;

import com.empresa.demo.domain.dto.PostDto;

import java.util.List;

public interface ExternalPostClient {

    List<PostDto> fetchPosts();

}
