package com.myblog4.myblog4.service.impl;

import com.myblog4.myblog4.entity.Post;
import com.myblog4.myblog4.exception.ResourceNotFound;
import com.myblog4.myblog4.payload.PostDto;
import com.myblog4.myblog4.payload.PostResponse;
import com.myblog4.myblog4.repository.PostRepository;
import com.myblog4.myblog4.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto savePost(PostDto postDto) {

        Post post = mapToEntity(postDto);
        Post savedPost = postRepository.save(post);
        PostDto dto = mapToDto(savedPost);

        return dto;
    }

    @Override
    public void deleteById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post is not found with id: " + id)
        );
        postRepository.deleteById(id);
    }

    @Override
    public PostDto getPostById(long id) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with post id: " + id)
        );

        PostDto postDto = mapToDto(post);
        return postDto;
    }

    @Override
    public PostDto updatePostById(long id, PostDto postDto) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with id: " + id)
        );

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post savedPost = postRepository.save(post);

        PostDto dto = mapToDto(savedPost);

        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> pagePostObject = postRepository.findAll(pageable);
        List<Post> posts = pagePostObject.getContent();
        List<PostDto> allDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setDtos(allDtos);
        postResponse.setPageNo(pagePostObject.getNumber());
        postResponse.setPageSize(pagePostObject.getSize());
        postResponse.setTotalPages(pagePostObject.getTotalPages());
        postResponse.setLastPage(pagePostObject.isLast());

        return postResponse;
    }


    Post mapToEntity(PostDto postDto){

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());

        Post post = modelMapper.map(postDto, Post.class);

        return post;
    }


    PostDto mapToDto(Post post){

//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());


        PostDto dto = modelMapper.map(post, PostDto.class);

                return dto;
    }
}
