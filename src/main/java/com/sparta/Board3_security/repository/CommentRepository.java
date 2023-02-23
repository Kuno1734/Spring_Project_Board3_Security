package com.sparta.Board3_security.repository;


import com.sparta.Board3_security.entity.Comment;
import com.sparta.Board3_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);
}
