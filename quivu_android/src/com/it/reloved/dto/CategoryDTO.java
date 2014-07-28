package com.it.reloved.dto;

import java.util.List;

public class CategoryDTO {

	String success = "", msg = "";
	List<CategoryItemDTO> CategoryList = null;

	public CategoryDTO(String success, String msg,
			List<CategoryItemDTO> categoryItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
		this.CategoryList = categoryItemDTOs;
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

	public List<CategoryItemDTO> getCategoryItemDTOs() {
		return CategoryList;
	}

	public void setCategoryItemDTOs(List<CategoryItemDTO> categoryItemDTOs) {
		this.CategoryList = categoryItemDTOs;
	}

}
