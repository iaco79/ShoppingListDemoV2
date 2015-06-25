package com.iaco.testapp.dto;



public class Item extends BaseDto {

	private int itemId;
	private String name;
	private String description;
	private String pictureName;
	public String getPictureName() {
		return pictureName;
	}

	public boolean isToDelete() {
		return toDelete;
	}

	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
	}

	private boolean toDelete=false;

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	private int status = 0;
	
	
	public String getIdString()
	{
		return String.valueOf(itemId);
	}
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		
		if(this.status != status)
		 setDirty(true);
		
		this.status = status;
	}


	public int getItemId() {
		return itemId;
	}

	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
