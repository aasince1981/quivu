package com.it.reloved.dto;

public class ActivityItemDTO {

	String ActivityTypeId = "", ActivityFromUserId = "",
			ActivityFromUserName = "", ActivityFromUserImage = "",
			ActivityProductId = "", ActivityProductName = "",
			ActivityProductImage = "", ActivityProductMessage = "",
			ActivityOfferPrice = "", ActivityMessage = "", ActivityTime = "",
			ActivityFeedbackImage = "";

	public ActivityItemDTO(String activityTypeId, String activityFromUserId,
			String activityFromUserName, String activityFromUserImage,
			String activityProductId, String activityProductName,
			String activityProductImage, String activityProductMessage,
			String activityOfferPrice, String activityMessage,
			String activityTime, String ActivityFeedbackImage) {
		super();
		ActivityTypeId = activityTypeId;
		ActivityFromUserId = activityFromUserId;
		ActivityFromUserName = activityFromUserName;
		ActivityFromUserImage = activityFromUserImage;
		ActivityProductId = activityProductId;
		ActivityProductName = activityProductName;
		ActivityProductImage = activityProductImage;
		ActivityProductMessage = activityProductMessage;
		ActivityOfferPrice = activityOfferPrice;
		ActivityMessage = activityMessage;
		ActivityTime = activityTime;
		this.ActivityFeedbackImage = ActivityFeedbackImage;
	}

	public String getActivityFeedbackImage() {
		return ActivityFeedbackImage;
	}

	public void setActivityFeedbackImage(String activityFeedbackImage) {
		ActivityFeedbackImage = activityFeedbackImage;
	}

	public String getActivityTypeId() {
		return ActivityTypeId;
	}

	public void setActivityTypeId(String activityTypeId) {
		ActivityTypeId = activityTypeId;
	}

	public String getActivityFromUserId() {
		return ActivityFromUserId;
	}

	public void setActivityFromUserId(String activityFromUserId) {
		ActivityFromUserId = activityFromUserId;
	}

	public String getActivityFromUserName() {
		return ActivityFromUserName;
	}

	public void setActivityFromUserName(String activityFromUserName) {
		ActivityFromUserName = activityFromUserName;
	}

	public String getActivityFromUserImage() {
		return ActivityFromUserImage;
	}

	public void setActivityFromUserImage(String activityFromUserImage) {
		ActivityFromUserImage = activityFromUserImage;
	}

	public String getActivityProductId() {
		return ActivityProductId;
	}

	public void setActivityProductId(String activityProductId) {
		ActivityProductId = activityProductId;
	}

	public String getActivityProductName() {
		return ActivityProductName;
	}

	public void setActivityProductName(String activityProductName) {
		ActivityProductName = activityProductName;
	}

	public String getActivityProductImage() {
		return ActivityProductImage;
	}

	public void setActivityProductImage(String activityProductImage) {
		ActivityProductImage = activityProductImage;
	}

	public String getActivityProductMessage() {
		return ActivityProductMessage;
	}

	public void setActivityProductMessage(String activityProductMessage) {
		ActivityProductMessage = activityProductMessage;
	}

	public String getActivityOfferPrice() {
		return ActivityOfferPrice;
	}

	public void setActivityOfferPrice(String activityOfferPrice) {
		ActivityOfferPrice = activityOfferPrice;
	}

	public String getActivityMessage() {
		return ActivityMessage;
	}

	public void setActivityMessage(String activityMessage) {
		ActivityMessage = activityMessage;
	}

	public String getActivityTime() {
		return ActivityTime;
	}

	public void setActivityTime(String activityTime) {
		ActivityTime = activityTime;
	}

}
