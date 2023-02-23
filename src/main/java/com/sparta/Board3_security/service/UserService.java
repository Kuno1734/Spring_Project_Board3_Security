package com.sparta.Board3_security.service;


import com.sparta.Board3_security.dto.requestDto.LoginRequestDto;
import com.sparta.Board3_security.dto.requestDto.SignupRequestDto;
import com.sparta.Board3_security.dto.responseDto.MessageResponseDto;
import com.sparta.Board3_security.entity.User;
import com.sparta.Board3_security.entity.UserRoleEnum;
import com.sparta.Board3_security.exception.CustomException;
import com.sparta.Board3_security.exception.Exception;
import com.sparta.Board3_security.jwt.JwtUtil;
import com.sparta.Board3_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.Board3_security.exception.Exception.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<Object> signup(SignupRequestDto requestDto) {

        //SignupRequestDto 에서 username과 password를 받아 password는 엔코드한다.
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        //username의 중복확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(DUPLICATED_USERNAME);

        }
        // User Role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.getAdmin()){
            if(!requestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new CustomException(INVALID_TOKEN);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 입력한 username, password -> requestDto 로 user객체를 만들어서 repository에 저장
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
        userRepository.save(user);
        // 가입성공 메시지 전달
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto("회원가입 완료",HttpStatus.OK));
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Object> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(Exception.NOT_FOUND_USER)
        );
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(Exception.INVALID_PASSWORD);
        }
        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                .message("로그인 성공")
                .statusCode(HttpStatus.OK)
                .build();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return ResponseEntity.status(HttpStatus.OK).body(messageResponseDto);
    }
}
