package com.it.reloved.dto;

public class MessageItemDTO {

	String MessageId = "", MessageFromUserId = "", MessageFromUserName = "",
			MessageFromUserImage = "", MessageToUserId = "", Message = "",
			MessageAddTime = "", MessageType = "";

	public MessageItemDTO(String messageId, String messageFromUserId,
			String messageFromUserName, String messageFromUserImage,
			String messageToUserId, String message, String messageAddTime,
			String messageType) {
		super();
		MessageId = messageId;
		MessageFromUserId = messageFromUserId;
		MessageFromUserName = messageFromUserName;
		MessageFromUserImage = messageFromUserImage;
		MessageToUserId = messageToUserId;
		Message = message;
		MessageAddTime = messageAddTime;
		MessageType = messageType;
	}

	public String getMessageId() {
		return MessageId;
	}

	public void setMessageId(String messageId) {
		MessageId = messageId;
	}

	public String getMessageFromUserId() {
		return MessageFromUserId;
	}

	public void setMessageFromUserId(String messageFromUserId) {
		MessageFromUserId = messageFromUserId;
	}

	public String getMessageFromUserName() {
		return MessageFromUserName;
	}

	public void setMessageFromUserName(String messageFromUserName) {
		MessageFromUserName = messageFromUserName;
	}

	public String getMessageFromUserImage() {
		return MessageFromUserImage;
	}

	public void setMessageFromUserImage(String messageFromUserImage) {
		MessageFromUserImage = messageFromUserImage;
	}

	public String getMessageToUserId() {
		return MessageToUserId;
	}

	public void setMessageToUserId(String messageToUserId) {
		MessageToUserId = messageToUserId;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getMessageAddTime() {
		return MessageAddTime;
	}

	public void setMessageAddTime(String messageAddTime) {
		MessageAddTime = messageAddTime;
	}

	public String getMessageType() {
		return MessageType;
	}

	public void setMessageType(String messageType) {
		MessageType = messageType;
	}

}
