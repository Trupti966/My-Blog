package com.myblog4.myblog4.repository;

import com.myblog4.myblog4.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findCommentByPostId(long postId);

}
