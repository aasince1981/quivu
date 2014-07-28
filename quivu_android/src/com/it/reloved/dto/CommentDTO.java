package com.it.reloved.dto;

import java.util.ArrayList;
import java.util.List;

public class CommentDTO {

	String success = "", msg = "";
	List<CommentItemDTO> commentItemDTOs = new ArrayList<CommentItemDTO>();

	public CommentDTO(String success, String msg,
			List<CommentItemDTO> commentItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.commentItemDTOs = commentItemDTOs;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<CommentItemDTO> getCommentItemDTOs() {
		return commentItemDTOs;
	}

	public void setCommentItemDTOs(List<CommentItemDTO> commentItemDTOs) {
		this.commentItemDTOs = commentItemDTOs;
	}

}
