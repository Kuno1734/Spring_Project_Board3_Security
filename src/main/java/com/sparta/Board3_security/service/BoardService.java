package com.sparta.Board3_security.service;

import com.sparta.Board3_security.dto.requestDto.BoardRequestDto;
import com.sparta.Board3_security.dto.responseDto.BoardResponseDto;
import com.sparta.Board3_security.dto.responseDto.MessageResponseDto;
import com.sparta.Board3_security.entity.Board;
import com.sparta.Board3_security.entity.Comment;
import com.sparta.Board3_security.entity.User;
import com.sparta.Board3_security.entity.UserRoleEnum;
import com.sparta.Board3_security.exception.CustomException;
import com.sparta.Board3_security.jwt.JwtUtil;
import com.sparta.Board3_security.repository.BoardRepository;
import com.sparta.Board3_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.sparta.Board3_security.exception.Exception.AUTHORIZATION;
import static com.sparta.Board3_security.exception.Exception.NOT_FOUND_BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /*  1.전체 게시글 조회 - 모든 게시글을 볼 수 있다.
        2.선택 게시글 조회 - 게시글의 id값을 받아 검색한다.
        3.게시글 작성 - 로그인한 유저(인증된 유저)만이 게시글을 작성할 수 있다.
        - 여기서 예외 발생시 throw CustomException 넘긴다 이는 CustonExceptionHandler의 @ExceptionHandler를 통해 처리된다.CustomException.
        4.게시글 수정 -
                */
    //전체 게시글 조회 -------------------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getBoard() {

        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();

        // 댓글리스트 작성일자 기준 내림차순 정렬
        for (Board board : boardList) {
            board.getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        }
        List<BoardResponseDto> responseDto = boardList.stream().map(BoardResponseDto::new).toList();

        return ResponseEntity.ok(responseDto);

    }

    //선택한 게시글 조회 -------------------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getBoard(Long id) {
        //id사용하여 DB조회 후 게시글 존제 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new CustomException(NOT_FOUND_BOARD);
        }
        board.get().getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        return ResponseEntity.ok().body(new BoardResponseDto(board.get()));
    }

    //게시글 작성 -------------------------------------------------------------------------------------
    @Transactional
    public ResponseEntity<Object> createBoard(BoardRequestDto requestDto, User user) {
        Board createBoard = boardRepository.save(new Board(requestDto, user));

        return ResponseEntity.ok().body(new BoardResponseDto(createBoard));
    }


    //게시글 업데이트 -------------------------------------------------------------------------------------
    @Transactional
    public ResponseEntity<Object> update(Long id, BoardRequestDto requestDto, User user) {

        //아이디를 통해 게시글이 DB에 존제하는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new CustomException(NOT_FOUND_BOARD);
        }
        //게시글의 id와 토큰의 user id가 일치하는지 확인  /  수정하는 유저가 관리자라면 수정가능
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(AUTHORIZATION);
        }

        board.get().update(requestDto, user);
        boardRepository.saveAndFlush(board.get()); // responseDto에 modifedAt 업데이트 해주기 위해 saveAndFlush 사용

        return ResponseEntity.ok().body(new BoardResponseDto(board.get()));

    }

    //게시글 삭제 -------------------------------------------------------------------------------------
    @Transactional
    public ResponseEntity delete(Long id, User user) {

        //아이디를 통해 게시글이 DB에 존제하는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new CustomException(NOT_FOUND_BOARD);
        }

        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(AUTHORIZATION);
        }

        boardRepository.deleteById(id);
        return ResponseEntity.ok().body(MessageResponseDto.builder()
                .message("삭제완료!")
                .statusCode(HttpStatus.OK)
                .build());
    }
}



