package com.it.reloved.dto;

public class CommentItemDTO {

	String CommentUserId = "", CommentUserName = "", CommentUserImage = "",
			CommentMessage = "", CommentToReplyUserId = "",
			ReplyToUserName = "", CommentTime = "", CommentOwnerStatus = "",
			CommentReplyStaus = "", CommentId = "";

	public CommentItemDTO(String commentUserId, String commentUserName,
			String commentUserImage, String commentMessage,
			String commentToReplyUserId, String replyToUserName,
			String commentTime, String commentOwnerStatus,
			String commentReplyStaus, String CommentId) {
		super();
		CommentUserId = commentUserId;
		CommentUserName = commentUserName;
		CommentUserImage = commentUserImage;
		CommentMessage = commentMessage;
		CommentToReplyUserId = commentToReplyUserId;
		ReplyToUserName = replyToUserName;
		CommentTime = commentTime;
		CommentOwnerStatus = commentOwnerStatus;
		CommentReplyStaus = commentReplyStaus;
		this.CommentId = CommentId;
	}

	public String getCommentId() {
		return CommentId;
	}

	public void setCommentId(String commentId) {
		CommentId = commentId;
	}

	public String getCommentUserId() {
		return CommentUserId;
	}

	public void setCommentUserId(String commentUserId) {
		CommentUserId = commentUserId;
	}

	public String getCommentUserName() {
		return CommentUserName;
	}

	public void setCommentUserName(String commentUserName) {
		CommentUserName = commentUserName;
	}

	public String getCommentUserImage() {
		return CommentUserImage;
	}

	public void setCommentUserImage(String commentUserImage) {
		CommentUserImage = commentUserImage;
	}

	public String getCommentMessage() {
		return CommentMessage;
	}

	public void setCommentMessage(String commentMessage) {
		CommentMessage = commentMessage;
	}

	public String getCommentToReplyUserId() {
		return CommentToReplyUserId;
	}

	public void setCommentToReplyUserId(String commentToReplyUserId) {
		CommentToReplyUserId = commentToReplyUserId;
	}

	public String getReplyToUserName() {
		return ReplyToUserName;
	}

	public void setReplyToUserName(String replyToUserName) {
		ReplyToUserName = replyToUserName;
	}

	public String getCommentTime() {
		return CommentTime;
	}

	public void setCommentTime(String commentTime) {
		CommentTime = commentTime;
	}

	public String getCommentOwnerStatus() {
		return CommentOwnerStatus;
	}

	public void setCommentOwnerStatus(String commentOwnerStatus) {
		CommentOwnerStatus = commentOwnerStatus;
	}

	public String getCommentReplyStaus() {
		return CommentReplyStaus;
	}

	public void setCommentReplyStaus(String commentReplyStaus) {
		CommentReplyStaus = commentReplyStaus;
	}

}
