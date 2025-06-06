package com.codewitharrays.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CategoryDTO {
	
	private Integer id;
	private String name;
	
	private String description;
	
	private Boolean isActive;
	
	private Boolean isDelete;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Integer updatedBy;
	
	private Date updatedDate;
}
