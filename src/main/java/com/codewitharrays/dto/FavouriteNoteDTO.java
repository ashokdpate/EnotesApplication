package com.codewitharrays.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteNoteDTO {
	
	private Integer id;
	
	private NotesDTO notes;
	
	private Integer userId;
}
