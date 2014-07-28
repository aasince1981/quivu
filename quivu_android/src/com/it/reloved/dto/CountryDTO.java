package com.it.reloved.dto;

import java.util.List;

public class CountryDTO {
	String success = "", msg = "";
	List<ItemDTO> itemDTOs = null;

	public CountryDTO(String success, String msg, List<ItemDTO> itemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.itemDTOs = itemDTOs;
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

	public List<ItemDTO> getItemDTOs() {
		return itemDTOs;
	}

	public void setItemDTOs(List<ItemDTO> itemDTOs) {
		this.itemDTOs = itemDTOs;
	}

}
