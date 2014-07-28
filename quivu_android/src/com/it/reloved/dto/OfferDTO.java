package com.it.reloved.dto;

import java.util.List;

public class OfferDTO {

	String success, msg;
	List<OfferItemDTO> offerItemDTOs = null;

	public OfferDTO(String success, String msg, List<OfferItemDTO> offerItemDTOs) {
		super();
		this.success = success;
		this.msg = msg;
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

	public List<OfferItemDTO> getOfferItemDTOs() {
		return offerItemDTOs;
	}

	public void setOfferItemDTOs(List<OfferItemDTO> offerItemDTOs) {
		this.offerItemDTOs = offerItemDTOs;
	}

}
