package com.it.reloved.dto;

import java.util.ArrayList;
import java.util.List;

public class ActivityDTO {

	String success = "", msg = "";
	List<ActivityItemDTO> Activities = new ArrayList<ActivityItemDTO>();
	
	public ActivityDTO() { }

	public ActivityDTO(String success, String msg,
			List<ActivityItemDTO> activityItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.Activities = activityItemDTOs;
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

	public List<ActivityItemDTO> getActivityItemDTOs() {
		return Activities;
	}

	public void setActivityItemDTOs(List<ActivityItemDTO> activityItemDTOs) {
		this.Activities = activityItemDTOs;
	}

}
