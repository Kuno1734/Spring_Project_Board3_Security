package com.sparta.Board3_security.service;

import com.sparta.Board3_security.dto.responseDto.MessageResponseDto;
import com.sparta.Board3_security.entity.Board;
import com.sparta.Board3_security.entity.Comment;
import com.sparta.Board3_security.entity.Likes;
import com.sparta.Board3_security.exception.CustomException;
import com.sparta.Board3_security.repository.BoardRepository;
import com.sparta.Board3_security.repository.CommentRepository;
import com.sparta.Board3_security.repository.LikeRepository;
import com.sparta.Board3_security.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sparta.Board3_security.exception.Exception.NOT_FOUND_BOARD;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    public ResponseEntity LikeBoard(Long id, UserDetailsImpl userDetails) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        Optional<Likes> found = likeRepository.findByBoardAndUser(board, userDetails.getUser());
        if (found.isEmpty()) {
            Likes likeBoard = Likes.builder()
                    .board(board)
                    .user(userDetails.getUser())
                    .build();
            likeRepository.save(likeBoard);
            return ResponseEntity.ok().body(MessageResponseDto.builder()
                    .message("게시글 좋아요!")
                    .statusCode(HttpStatus.OK)
                    .build());
        } else {
            likeRepository.delete(found.get());
            likeRepository.flush();
            return ResponseEntity.ok().body(MessageResponseDto.builder()
                    .message("게시글 좋아요 취소!")
                    .statusCode(HttpStatus.OK)
                    .build());
        }
    }

    public ResponseEntity LikeComment(Long id, UserDetailsImpl userDetails) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        Optional<Likes> found = likeRepository.findByCommentAndUser(comment, userDetails.getUser());
        if (found.isEmpty()) {
            Likes likeComment = Likes.builder()
                    .comment(comment)
                    .user(userDetails.getUser())
                    .build();
            likeRepository.save(likeComment);
            return ResponseEntity.ok().body(MessageResponseDto.builder()
                    .message(" 댓글 좋아요!")
                    .statusCode(HttpStatus.OK)
                    .build());
        } else {
            likeRepository.delete(found.get());
            likeRepository.flush();
            return ResponseEntity.ok().body(MessageResponseDto.builder()
                    .message(" 댓글 좋아요 취소!")
                    .statusCode(HttpStatus.OK)
                    .build());
        }
    }

}
