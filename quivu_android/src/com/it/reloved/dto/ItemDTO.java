package com.it.reloved.dto;

import java.util.List;

public class ItemDTO {

	String id, name;
	List<ItemDTO> itemDTOs;

	public ItemDTO(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ItemDTO(String id, String name, List<ItemDTO> itemDTOs) {
		super();
		this.id = id;
		this.name = name;
		this.itemDTOs = itemDTOs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemDTO> getItemDTOs() {
		return itemDTOs;
	}

	public void setItemDTOs(List<ItemDTO> itemDTOs) {
		this.itemDTOs = itemDTOs;
	}

}
