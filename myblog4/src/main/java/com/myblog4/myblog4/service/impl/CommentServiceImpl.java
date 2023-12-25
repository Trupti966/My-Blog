package com.myblog4.myblog4.service.impl;

import com.myblog4.myblog4.entity.Comment;
import com.myblog4.myblog4.entity.Post;
import com.myblog4.myblog4.exception.ResourceNotFound;
import com.myblog4.myblog4.payload.CommentDto;
import com.myblog4.myblog4.repository.CommentRepository;
import com.myblog4.myblog4.repository.PostRepository;
import com.myblog4.myblog4.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {


    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id: " + postId)
        );

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        CommentDto dto = mapToDto(savedComment);
        return dto;

    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id: " + postId)
        );

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {

        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        List<CommentDto> commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

        return commentDtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFound("Comment not found with commentId : " + commentId)
        );

        comment.setBody(commentDto.getBody());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());

        Comment savedComment = commentRepository.save(comment);

        CommentDto dto = mapToDto(savedComment);

        return dto;
    }

    Comment mapToEntity(CommentDto commentDto){

//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());

        Comment comment = modelMapper.map(commentDto, Comment.class);

        return comment;
    }

    CommentDto mapToDto(Comment comment){

//        CommentDto dto = new CommentDto();
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setBody(comment.getBody());

        CommentDto dto = modelMapper.map(comment, CommentDto.class);

        return dto;
    }


}
