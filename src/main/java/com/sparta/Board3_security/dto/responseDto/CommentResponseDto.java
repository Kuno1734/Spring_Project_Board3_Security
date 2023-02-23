package com.sparta.Board3_security.dto.responseDto;

import com.sparta.Board3_security.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private String usernaem;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long commentLikeCount;

    @Builder
    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.usernaem = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikeCount = comment.getCommentLikes().stream().count();
    }

}
