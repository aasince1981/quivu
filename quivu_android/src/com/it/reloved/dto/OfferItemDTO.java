package com.it.reloved.dto;

public class OfferItemDTO {

	String OfferUserId = "", OfferUserName = "", OfferUserImage = "",
			OfferProductId = "", OfferAmount = "", OfferAddTime = "",
			OfferMessage = "";

	public OfferItemDTO(String offerUserId, String offerUserName,
			String offerUserImage, String offerProductId, String offerAmount,
			String offerAddTime, String OfferMessage) {
		super();
		OfferUserId = offerUserId;
		OfferUserName = offerUserName;
		OfferUserImage = offerUserImage;
		OfferProductId = offerProductId;
		OfferAmount = offerAmount;
		OfferAddTime = offerAddTime;
		this.OfferMessage = OfferMessage;
	}

	public String getOfferMessage() {
		return OfferMessage;
	}

	public void setOfferMessage(String offerMessage) {
		OfferMessage = offerMessage;
	}

	public String getOfferUserId() {
		return OfferUserId;
	}

	public void setOfferUserId(String offerUserId) {
		OfferUserId = offerUserId;
	}

	public String getOfferUserName() {
		return OfferUserName;
	}

	public void setOfferUserName(String offerUserName) {
		OfferUserName = offerUserName;
	}

	public String getOfferUserImage() {
		return OfferUserImage;
	}

	public void setOfferUserImage(String offerUserImage) {
		OfferUserImage = offerUserImage;
	}

	public String getOfferProductId() {
		return OfferProductId;
	}

	public void setOfferProductId(String offerProductId) {
		OfferProductId = offerProductId;
	}

	public String getOfferAmount() {
		return OfferAmount;
	}

	public void setOfferAmount(String offerAmount) {
		OfferAmount = offerAmount;
	}

	public String getOfferAddTime() {
		return OfferAddTime;
	}

	public void setOfferAddTime(String offerAddTime) {
		OfferAddTime = offerAddTime;
	}

}
