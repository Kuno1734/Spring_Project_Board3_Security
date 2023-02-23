package com.sparta.Board3_security.repository;

import com.sparta.Board3_security.entity.Board;
import com.sparta.Board3_security.entity.Comment;
import com.sparta.Board3_security.entity.Likes;
import com.sparta.Board3_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByBoardAndUser(Board board , User user);
    Optional<Likes> findByCommentAndUser(Comment comment, User user);

}
