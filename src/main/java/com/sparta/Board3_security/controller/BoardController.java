package com.sparta.Board3_security.controller;

import com.sparta.Board3_security.dto.requestDto.BoardRequestDto;
import com.sparta.Board3_security.dto.responseDto.BoardResponseDto;
import com.sparta.Board3_security.entity.User;
import com.sparta.Board3_security.security.UserDetailsImpl;
import com.sparta.Board3_security.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<List<BoardResponseDto>> getBoard() { return boardService.getBoard(); }

    @GetMapping("/board/{id}")
    public ResponseEntity getBoard(@PathVariable Long id){
        return boardService.getBoard(id);
    }

    @PostMapping("/board")
    public ResponseEntity createBoard(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails ){
        return boardService.createBoard(requestDto, userDetails.getUser()) ;
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.update(id,requestDto,userDetails.getUser());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.delete(id,userDetails.getUser());
    }

}
