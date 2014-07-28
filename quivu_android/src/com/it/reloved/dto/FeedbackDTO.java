package com.it.reloved.dto;

import java.util.List;

public class FeedbackDTO {

	String success = "", msg = "";
	List<FeedbackItemDTO> feedbackItemDTOs = null;

	public FeedbackDTO(String success, String msg,
			List<FeedbackItemDTO> feedbackItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.feedbackItemDTOs = feedbackItemDTOs;
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

	public List<FeedbackItemDTO> getFeedbackItemDTOs() {
		return feedbackItemDTOs;
	}

	public void setFeedbackItemDTOs(List<FeedbackItemDTO> feedbackItemDTOs) {
		this.feedbackItemDTOs = feedbackItemDTOs;
	}

}
