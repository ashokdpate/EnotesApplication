package com.codewitharrays.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotesResponse {
		private List<NotesDTO> notes;
		private Integer pageNo;
		private Integer pageSize;
		private Long totalElements;
		private Integer totalPages;
		private Boolean isFirst;
		private Boolean isLast;
		
}
