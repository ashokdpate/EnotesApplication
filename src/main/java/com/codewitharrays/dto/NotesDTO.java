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
	
	private FileDto fileDetails;
	@Data
	public static class FileDto{
		private Integer id;	
		private String originalFileName;
		private String displayFileName;
	}
	
	
	@Data
	public static class CategoryDTO{
		private Integer id;
		private String name;
	}
}
