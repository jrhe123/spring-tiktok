package com.imooc.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentBO {

	private String vlogerId;
	
	private String fatherCommentId;
	
	private String vlogId;
	
	private String commentUserId;
	
	private String content;
}
