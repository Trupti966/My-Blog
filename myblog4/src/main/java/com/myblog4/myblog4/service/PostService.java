package com.myblog4.myblog4.service;

import com.myblog4.myblog4.payload.PostDto;
import com.myblog4.myblog4.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto);

    void deleteById(long id);

    PostDto getPostById(long id);

    PostDto updatePostById(long id, PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
