package com.myblog4.myblog4.service;

import com.myblog4.myblog4.payload.CommentDto;

import java.util.List;

public interface CommentService {

    public CommentDto createComment(long postId, CommentDto commentDto);

    public void deleteCommentById(long postId, long commentId);


    List<CommentDto> getCommentByPostId(long postId);

    CommentDto updateComment(long commentId, CommentDto commentDto);
}
