package com.sparta.Board3_security.controller;

import com.sparta.Board3_security.dto.requestDto.LoginRequestDto;
import com.sparta.Board3_security.dto.requestDto.SignupRequestDto;
import com.sparta.Board3_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody SignupRequestDto userRequestDto){
        return userService.signup(userRequestDto);
    }
    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto userRequestDto, HttpServletResponse response){
        return userService.login(userRequestDto,response);
    }
}