package com.it.reloved.dto;

public class CategoryItemDTO {

	String CategoryId, CategoryName, CategoryImage, CategoryPrice,
			CategoryLikeCount;
	String ProductUserId = "", ProductUserName = "", ProductUserImage = "",
			ProductAddDate = "", ProductWebsiteUrl, LikeStatus,
			ProductSoldStatus = "", FollowStatus = "";

	public CategoryItemDTO(String categoryId, String categoryName,
			String categoryImage) {
		super();
		CategoryId = categoryId;
		CategoryName = categoryName;
		CategoryImage = categoryImage;
	}

	public CategoryItemDTO(String categoryId, String categoryName,
			String categoryImage, String FollowStatus) {
		super();
		CategoryId = categoryId;
		CategoryName = categoryName;
		CategoryImage = categoryImage;
		this.FollowStatus = FollowStatus;
	}

	public CategoryItemDTO(String categoryId, String categoryName,
			String categoryImage, String categoryPrice, String categoryLikeCount) {
		super();
		CategoryId = categoryId;
		CategoryName = categoryName;
		CategoryImage = categoryImage;
		CategoryPrice = categoryPrice;
		CategoryLikeCount = categoryLikeCount;
	}

	public CategoryItemDTO(String categoryId, String categoryName,
			String categoryImage, String categoryPrice,
			String categoryLikeCount, String productUserId,
			String productUserName, String productUserImage,
			String productAddDate, String ProductWebsiteUrl, String LikeStatus,
			String ProductSoldStatus) {
		super();
		CategoryId = categoryId;
		CategoryName = categoryName;
		CategoryImage = categoryImage;
		CategoryPrice = categoryPrice;
		CategoryLikeCount = categoryLikeCount;
		ProductUserId = productUserId;
		ProductUserName = productUserName;
		ProductUserImage = productUserImage;
		ProductAddDate = productAddDate;
		this.ProductWebsiteUrl = ProductWebsiteUrl;
		this.LikeStatus = LikeStatus;
		this.ProductSoldStatus = ProductSoldStatus;
	}

	public String getFollowStatus() {
		return FollowStatus;
	}

	public void setFollowStatus(String followStatus) {
		FollowStatus = followStatus;
	}

	public String getProductSoldStatus() {
		return ProductSoldStatus;
	}

	public void setProductSoldStatus(String productSoldStatus) {
		ProductSoldStatus = productSoldStatus;
	}

	public String getProductWebsiteUrl() {
		return ProductWebsiteUrl;
	}

	public void setProductWebsiteUrl(String productWebsiteUrl) {
		ProductWebsiteUrl = productWebsiteUrl;
	}

	public String getLikeStatus() {
		return LikeStatus;
	}

	public void setLikeStatus(String likeStatus) {
		LikeStatus = likeStatus;
	}

	public String getCategoryPrice() {
		return CategoryPrice;
	}

	public void setCategoryPrice(String categoryPrice) {
		CategoryPrice = categoryPrice;
	}

	public String getCategoryLikeCount() {
		return CategoryLikeCount;
	}

	public void setCategoryLikeCount(String categoryLikeCount) {
		CategoryLikeCount = categoryLikeCount;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getCategoryImage() {
		return CategoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		CategoryImage = categoryImage;
	}

	public String getProductUserId() {
		return ProductUserId;
	}

	public void setProductUserId(String productUserId) {
		ProductUserId = productUserId;
	}

	public String getProductUserName() {
		return ProductUserName;
	}

	public void setProductUserName(String productUserName) {
		ProductUserName = productUserName;
	}

	public String getProductUserImage() {
		return ProductUserImage;
	}

	public void setProductUserImage(String productUserImage) {
		ProductUserImage = productUserImage;
	}

	public String getProductAddDate() {
		return ProductAddDate;
	}

	public void setProductAddDate(String productAddDate) {
		ProductAddDate = productAddDate;
	}

}
