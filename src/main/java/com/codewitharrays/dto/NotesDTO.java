package com.codewitharrays.dto;

import java.util.Date;





import lombok.Data;

@Data
public class NotesDTO {
	
	private Integer id;
	private String title;
	private String description;
	private CategoryDTO category;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	
	@Data
	public static class CategoryDTO{
		private Integer id;
		private String name;
	}
}
