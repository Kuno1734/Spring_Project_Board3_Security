package com.sparta.Board3_security.service;

import com.sparta.Board3_security.dto.requestDto.CommentRequestDto;
import com.sparta.Board3_security.dto.responseDto.CommentResponseDto;
import com.sparta.Board3_security.dto.responseDto.MessageResponseDto;
import com.sparta.Board3_security.entity.Board;
import com.sparta.Board3_security.entity.Comment;
import com.sparta.Board3_security.entity.User;
import com.sparta.Board3_security.entity.UserRoleEnum;
import com.sparta.Board3_security.exception.CustomException;
import com.sparta.Board3_security.jwt.JwtUtil;
import com.sparta.Board3_security.repository.BoardRepository;
import com.sparta.Board3_security.repository.CommentRepository;
import com.sparta.Board3_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.sparta.Board3_security.exception.Exception.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<Object> createComment(Long id, CommentRequestDto requestDto, User user) {

        //선택한 게시글 조회
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new CustomException(NOT_FOUND_BOARD);
        }

        Comment comment = commentRepository.save(Comment.builder()
                .contents(requestDto.getContents())
                .board(board.get())
                .user(user)
                .build());
        return ResponseEntity.ok().body(new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<Object> updateComment(Long id, CommentRequestDto requestDto, User user) {

        //선택한 댓글 유무 조회
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new CustomException(NOT_FOUND_COMMENT);
        }

        Optional<Comment> found = commentRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(AUTHORIZATION);
        }

        comment.get().update(requestDto.getContents(), user);
//        commentRepository.saveAndFlush(comment.get());

        return ResponseEntity.ok(CommentResponseDto.builder()
                .comment(comment.get())
                .build());
    }


    public ResponseEntity<MessageResponseDto> deleteComment(Long id, User user) {

        //선택한 댓글 유무 조회
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new CustomException(NOT_FOUND_COMMENT);
        }

        Optional<Comment> found = commentRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(AUTHORIZATION);
        }

        commentRepository.deleteById(id);
        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("댓글 삭제 완료")
                .statusCode(HttpStatus.OK)
                .build());

    }
}


