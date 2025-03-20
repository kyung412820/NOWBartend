package com.nowbartend.domain.customer.post.repository;

import com.nowbartend.domain.customer.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuerydsl {
}
