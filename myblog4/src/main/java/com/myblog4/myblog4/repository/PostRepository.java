package com.myblog4.myblog4.repository;

import com.myblog4.myblog4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
