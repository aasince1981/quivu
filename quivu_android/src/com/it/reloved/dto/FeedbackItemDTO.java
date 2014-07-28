package com.it.reloved.dto;

public class FeedbackItemDTO {

	String FeedbackByUserId = "", FeedbackByUserName = "",
			FeedbackByUserImage = "", FeedbackToUserId = "",
			FeedbackToUserName = "", FeedbackType = "",
			FeedbackExperience = "", FeedbackDescription = "",
			FeedbackTime = "", FeedbackEditStatus = "", FeedbackEditTime = "",
			FeedbackReplyStatus = "", FeedbackReplyMessage = "",
			FeedbackReplyTime = "", FeedbackEditReplyTime = "",
			FeedbackToUserImage = "", FeedbackId = "";

	public FeedbackItemDTO(String feedbackByUserId, String feedbackByUserName,
			String feedbackByUserImage, String feedbackToUserId,
			String feedbackToUserName, String feedbackType,
			String feedbackExperience, String feedbackDescription,
			String feedbackTime, String feedbackEditStatus,
			String feedbackEditTime, String feedbackReplyStatus,
			String feedbackReplyMessage, String feedbackReplyTime,
			String feedbackEditReplyTime, String FeedbackToUserImage,
			String FeedbackId) {
		super();
		FeedbackByUserId = feedbackByUserId;
		FeedbackByUserName = feedbackByUserName;
		FeedbackByUserImage = feedbackByUserImage;
		FeedbackToUserId = feedbackToUserId;
		FeedbackToUserName = feedbackToUserName;
		FeedbackType = feedbackType;
		FeedbackExperience = feedbackExperience;
		FeedbackDescription = feedbackDescription;
		FeedbackTime = feedbackTime;
		FeedbackEditStatus = feedbackEditStatus;
		FeedbackEditTime = feedbackEditTime;
		FeedbackReplyStatus = feedbackReplyStatus;
		FeedbackReplyMessage = feedbackReplyMessage;
		FeedbackReplyTime = feedbackReplyTime;
		FeedbackEditReplyTime = feedbackEditReplyTime;
		this.FeedbackToUserImage = FeedbackToUserImage;
		this.FeedbackId = FeedbackId;
	}

	public String getFeedbackId() {
		return FeedbackId;
	}

	public void setFeedbackId(String feedbackId) {
		FeedbackId = feedbackId;
	}

	public String getFeedbackToUserImage() {
		return FeedbackToUserImage;
	}

	public void setFeedbackToUserImage(String feedbackToUserImage) {
		FeedbackToUserImage = feedbackToUserImage;
	}

	public String getFeedbackByUserId() {
		return FeedbackByUserId;
	}

	public void setFeedbackByUserId(String feedbackByUserId) {
		FeedbackByUserId = feedbackByUserId;
	}

	public String getFeedbackByUserName() {
		return FeedbackByUserName;
	}

	public void setFeedbackByUserName(String feedbackByUserName) {
		FeedbackByUserName = feedbackByUserName;
	}

	public String getFeedbackByUserImage() {
		return FeedbackByUserImage;
	}

	public void setFeedbackByUserImage(String feedbackByUserImage) {
		FeedbackByUserImage = feedbackByUserImage;
	}

	public String getFeedbackToUserId() {
		return FeedbackToUserId;
	}

	public void setFeedbackToUserId(String feedbackToUserId) {
		FeedbackToUserId = feedbackToUserId;
	}

	public String getFeedbackToUserName() {
		return FeedbackToUserName;
	}

	public void setFeedbackToUserName(String feedbackToUserName) {
		FeedbackToUserName = feedbackToUserName;
	}

	public String getFeedbackType() {
		return FeedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		FeedbackType = feedbackType;
	}

	public String getFeedbackExperience() {
		return FeedbackExperience;
	}

	public void setFeedbackExperience(String feedbackExperience) {
		FeedbackExperience = feedbackExperience;
	}

	public String getFeedbackDescription() {
		return FeedbackDescription;
	}

	public void setFeedbackDescription(String feedbackDescription) {
		FeedbackDescription = feedbackDescription;
	}

	public String getFeedbackTime() {
		return FeedbackTime;
	}

	public void setFeedbackTime(String feedbackTime) {
		FeedbackTime = feedbackTime;
	}

	public String getFeedbackEditStatus() {
		return FeedbackEditStatus;
	}

	public void setFeedbackEditStatus(String feedbackEditStatus) {
		FeedbackEditStatus = feedbackEditStatus;
	}

	public String getFeedbackEditTime() {
		return FeedbackEditTime;
	}

	public void setFeedbackEditTime(String feedbackEditTime) {
		FeedbackEditTime = feedbackEditTime;
	}

	public String getFeedbackReplyStatus() {
		return FeedbackReplyStatus;
	}

	public void setFeedbackReplyStatus(String feedbackReplyStatus) {
		FeedbackReplyStatus = feedbackReplyStatus;
	}

	public String getFeedbackReplyMessage() {
		return FeedbackReplyMessage;
	}

	public void setFeedbackReplyMessage(String feedbackReplyMessage) {
		FeedbackReplyMessage = feedbackReplyMessage;
	}

	public String getFeedbackReplyTime() {
		return FeedbackReplyTime;
	}

	public void setFeedbackReplyTime(String feedbackReplyTime) {
		FeedbackReplyTime = feedbackReplyTime;
	}

	public String getFeedbackEditReplyTime() {
		return FeedbackEditReplyTime;
	}

	public void setFeedbackEditReplyTime(String feedbackEditReplyTime) {
		FeedbackEditReplyTime = feedbackEditReplyTime;
	}

}
