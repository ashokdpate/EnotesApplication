package com.codewitharrays.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TodoDTO {
		private Integer id;
		private StatusDTO status;
		private String title;
		private Integer createdBy;
		private Date createdDate;		
		private Integer updatedBy;
		private Date updatedDate;

		
		@Data
		@Builder
		public static class StatusDTO{
			private Integer id;
			private String name;
		}
}

