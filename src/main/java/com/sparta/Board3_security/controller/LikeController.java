package com.sparta.Board3_security.controller;

import com.sparta.Board3_security.security.UserDetailsImpl;
import com.sparta.Board3_security.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/board/{id}")
    public ResponseEntity LikeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.LikeBoard(id,userDetails);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity LikeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.LikeComment(id, userDetails);
    }
}
