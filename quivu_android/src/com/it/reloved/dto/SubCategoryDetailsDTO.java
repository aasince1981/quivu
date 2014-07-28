package com.it.reloved.dto;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryDetailsDTO {

	String success = "", msg = "";
	String ProductUserName = "", ProductUserImage = "", ProductName = "",
			ProductPrice = "", ProductLikeCount = "", ProductCommentCount = "",
			ProductLatitude = "", ProductLongitude = "", ProductUserId = "",
			ProductDescription = "", ProductOfferCount = "",
			ProductAddDate = "", ProductWebsiteUrl = "", LikeStatus = "",
			ProductCatId = "", ProductAddress = "", ProductSoldStatus = "";
	List<CategoryItemDTO> productImageDtos = new ArrayList<CategoryItemDTO>();
	List<CommentItemDTO> commentItemDTOs = new ArrayList<CommentItemDTO>();
	List<CategoryItemDTO> offerItemDTOs = new ArrayList<CategoryItemDTO>();

	public SubCategoryDetailsDTO(String success, String msg,
			String ProductUserId, String productUserName,
			String productUserImage, String productName, String productPrice,
			String productLikeCount, String productCommentCount,
			String productLatitude, String productLongitude,
			String productDescription, List<CategoryItemDTO> productImageDtos,
			String LikeStatus, List<CommentItemDTO> commentItemDTOs,
			List<CategoryItemDTO> offerItemDTOs, String ProductOfferCount,
			String ProductAddDate, String ProductWebsiteUrl,
			String ProductCatId, String ProductAddress, String ProductSoldStatus) {
		super();
		this.success = success;
		this.msg = msg;
		this.ProductUserId = ProductUserId;
		ProductUserName = productUserName;
		ProductUserImage = productUserImage;
		ProductName = productName;
		ProductPrice = productPrice;
		ProductLikeCount = productLikeCount;
		ProductCommentCount = productCommentCount;
		ProductLatitude = productLatitude;
		ProductLongitude = productLongitude;
		ProductDescription = productDescription;
		this.productImageDtos = productImageDtos;
		this.LikeStatus = LikeStatus;
		this.commentItemDTOs = commentItemDTOs;
		this.offerItemDTOs = offerItemDTOs;
		this.ProductOfferCount = ProductOfferCount;
		this.ProductAddDate = ProductAddDate;
		this.ProductWebsiteUrl = ProductWebsiteUrl;
		this.ProductCatId = ProductCatId;
		this.ProductAddress = ProductAddress;
		this.ProductSoldStatus = ProductSoldStatus;

	}

	public String getProductSoldStatus() {
		return ProductSoldStatus;
	}

	public void setProductSoldStatus(String productSoldStatus) {
		ProductSoldStatus = productSoldStatus;
	}

	public String getProductAddress() {
		return ProductAddress;
	}

	public void setProductAddress(String productAddress) {
		ProductAddress = productAddress;
	}

	public String getProductCatId() {
		return ProductCatId;
	}

	public void setProductCatId(String productCatId) {
		ProductCatId = productCatId;
	}

	public String getProductWebsiteUrl() {
		return ProductWebsiteUrl;
	}

	public void setProductWebsiteUrl(String productWebsiteUrl) {
		ProductWebsiteUrl = productWebsiteUrl;
	}

	public String getProductOfferCount() {
		return ProductOfferCount;
	}

	public void setProductOfferCount(String productOfferCount) {
		ProductOfferCount = productOfferCount;
	}

	public String getProductAddDate() {
		return ProductAddDate;
	}

	public void setProductAddDate(String productAddDate) {
		ProductAddDate = productAddDate;
	}

	public String getProductUserId() {
		return ProductUserId;
	}

	public void setProductUserId(String productUserId) {
		ProductUserId = productUserId;
	}

	public List<CategoryItemDTO> getOfferItemDTOs() {
		return offerItemDTOs;
	}

	public void setOfferItemDTOs(List<CategoryItemDTO> offerItemDTOs) {
		this.offerItemDTOs = offerItemDTOs;
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

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(String productPrice) {
		ProductPrice = productPrice;
	}

	public String getProductLikeCount() {
		return ProductLikeCount;
	}

	public void setProductLikeCount(String productLikeCount) {
		ProductLikeCount = productLikeCount;
	}

	public String getProductCommentCount() {
		return ProductCommentCount;
	}

	public void setProductCommentCount(String productCommentCount) {
		ProductCommentCount = productCommentCount;
	}

	public String getProductLatitude() {
		return ProductLatitude;
	}

	public void setProductLatitude(String productLatitude) {
		ProductLatitude = productLatitude;
	}

	public String getProductLongitude() {
		return ProductLongitude;
	}

	public void setProductLongitude(String productLongitude) {
		ProductLongitude = productLongitude;
	}

	public String getProductDescription() {
		return ProductDescription;
	}

	public void setProductDescription(String productDescription) {
		ProductDescription = productDescription;
	}

	public List<CategoryItemDTO> getProductImageDtos() {
		return productImageDtos;
	}

	public void setProductImageDtos(List<CategoryItemDTO> productImageDtos) {
		this.productImageDtos = productImageDtos;
	}

	public String getLikeStatus() {
		return LikeStatus;
	}

	public void setLikeStatus(String likeStatus) {
		LikeStatus = likeStatus;
	}

	public List<CommentItemDTO> getCommentItemDTOs() {
		return commentItemDTOs;
	}

	public void setCommentItemDTOs(List<CommentItemDTO> commentItemDTOs) {
		this.commentItemDTOs = commentItemDTOs;
	}

}
