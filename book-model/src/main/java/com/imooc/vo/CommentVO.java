package com.imooc.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentVO {

    private String id;
    private String commentId;
    private String vlogerId;
    private String fatherCommentId;
    private String vlogId;
    private String commentUserId;
    private String commentUserNickname;
    private String commentUserFace;
    private String content;
    private Integer likeCounts;
    private String replyedUserNickname;
    private Date createTime;
    private Integer isLike = 0;
}
