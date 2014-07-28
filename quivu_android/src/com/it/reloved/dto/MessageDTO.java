package com.it.reloved.dto;

import java.util.ArrayList;
import java.util.List;

public class MessageDTO {

	String success = "", msg = "";
	List<MessageItemDTO> messageItemDTOs = new ArrayList<MessageItemDTO>();

	public MessageDTO(String success, String msg,
			List<MessageItemDTO> messageItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.messageItemDTOs = messageItemDTOs;
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

	public List<MessageItemDTO> getMessageItemDTOs() {
		return messageItemDTOs;
	}

	public void setMessageItemDTOs(List<MessageItemDTO> messageItemDTOs) {
		this.messageItemDTOs = messageItemDTOs;
	}

}
