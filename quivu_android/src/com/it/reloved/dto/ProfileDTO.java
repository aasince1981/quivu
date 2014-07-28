package com.it.reloved.dto;

import java.util.List;

public class ProfileDTO {

	String success = "", msg = "";
	String UserId = "", UserBio = "", UserEmailVerificationStatus = "",
			UserPositiveFeedBCount = "", UserNeutralFeedBCount = "",
			UserNegativeFeedBCount = "", UserFollowerCount = "",
			UserFollowingCount = "", OfferMadeByMe = "", StuffLikes = "",
					UserRegistationDate = "", UserWebsiteUrl="";
	List<CategoryItemDTO> productItemDTOs = null;
	String UserName = "", UserCity = "", UserCityName = "",
			UserDefaultCity = "", UserImage = "", FollowStatus = "";

	public ProfileDTO(String success, String msg, String userId,
			String userBio, String userEmailVerificationStatus,
			String userPositiveFeedBCount, String userNeutralFeedBCount,
			String userNegativeFeedBCount, String userFollowerCount,
			String userFollowingCount, String offerMadeByMe, 
			String stuffLikes, String userRegistationDate,			
			List<CategoryItemDTO> productItemDTOs, String userName,
			String userCity, String userCityName, String userDefaultCity,
			String userImage, String userWebsiteUrl, String FollowStatus) {
		super();
		this.success = success;
		this.msg = msg;
		UserId = userId;
		UserBio = userBio;
		UserEmailVerificationStatus = userEmailVerificationStatus;
		UserPositiveFeedBCount = userPositiveFeedBCount;
		UserNeutralFeedBCount = userNeutralFeedBCount;
		UserNegativeFeedBCount = userNegativeFeedBCount;
		UserFollowerCount = userFollowerCount;
		UserFollowingCount = userFollowingCount;
		OfferMadeByMe = offerMadeByMe; 
		StuffLikes = stuffLikes;
		UserRegistationDate = userRegistationDate;
		this.productItemDTOs = productItemDTOs;
		UserName = userName;
		UserCity = userCity;
		UserCityName = userCityName;
		UserDefaultCity = userDefaultCity;
		UserImage = userImage;
		UserWebsiteUrl = userWebsiteUrl;
		this.FollowStatus = FollowStatus;
	}

	public String getOfferMadeByMe() {
		return OfferMadeByMe;
	}

	public void setOfferMadeByMe(String offerMadeByMe) {
		OfferMadeByMe = offerMadeByMe;
	}

	public String getStuffLikes() {
		return StuffLikes;
	}

	public void setStuffLikes(String stuffLikes) {
		StuffLikes = stuffLikes;
	}

	public String getUserWebsiteUrl() {
		return UserWebsiteUrl;
	}

	public void setUserWebsiteUrl(String userWebsiteUrl) {
		UserWebsiteUrl = userWebsiteUrl;
	}

	public String getFollowStatus() {
		return FollowStatus;
	}

	public void setFollowStatus(String followStatus) {
		FollowStatus = followStatus;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserCity() {
		return UserCity;
	}

	public void setUserCity(String userCity) {
		UserCity = userCity;
	}

	public String getUserCityName() {
		return UserCityName;
	}

	public void setUserCityName(String userCityName) {
		UserCityName = userCityName;
	}

	public String getUserDefaultCity() {
		return UserDefaultCity;
	}

	public void setUserDefaultCity(String userDefaultCity) {
		UserDefaultCity = userDefaultCity;
	}

	public String getUserImage() {
		return UserImage;
	}

	public void setUserImage(String userImage) {
		UserImage = userImage;
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

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getUserBio() {
		return UserBio;
	}

	public void setUserBio(String userBio) {
		UserBio = userBio;
	}

	public String getUserEmailVerificationStatus() {
		return UserEmailVerificationStatus;
	}

	public void setUserEmailVerificationStatus(
			String userEmailVerificationStatus) {
		UserEmailVerificationStatus = userEmailVerificationStatus;
	}

	public String getUserPositiveFeedBCount() {
		return UserPositiveFeedBCount;
	}

	public void setUserPositiveFeedBCount(String userPositiveFeedBCount) {
		UserPositiveFeedBCount = userPositiveFeedBCount;
	}

	public String getUserNeutralFeedBCount() {
		return UserNeutralFeedBCount;
	}

	public void setUserNeutralFeedBCount(String userNeutralFeedBCount) {
		UserNeutralFeedBCount = userNeutralFeedBCount;
	}

	public String getUserNegativeFeedBCount() {
		return UserNegativeFeedBCount;
	}

	public void setUserNegativeFeedBCount(String userNegativeFeedBCount) {
		UserNegativeFeedBCount = userNegativeFeedBCount;
	}

	public String getUserFollowerCount() {
		return UserFollowerCount;
	}

	public void setUserFollowerCount(String userFollowerCount) {
		UserFollowerCount = userFollowerCount;
	}

	public String getUserFollowingCount() {
		return UserFollowingCount;
	}

	public void setUserFollowingCount(String userFollowingCount) {
		UserFollowingCount = userFollowingCount;
	}

	public String getUserRegistationDate() {
		return UserRegistationDate;
	}

	public void setUserRegistationDate(String userRegistationDate) {
		UserRegistationDate = userRegistationDate;
	}

	public List<CategoryItemDTO> getProductItemDTOs() {
		return productItemDTOs;
	}

	public void setProductItemDTOs(List<CategoryItemDTO> productItemDTOs) {
		this.productItemDTOs = productItemDTOs;
	}

}
