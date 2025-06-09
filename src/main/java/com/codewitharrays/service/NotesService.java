package com.codewitharrays.service;

import java.util.List;

import com.codewitharrays.dto.NotesDTO;

public interface NotesService {

	public Boolean saveNotes(NotesDTO notesDTO) throws Exception;
	
	public List<NotesDTO> getAllNotes();
}
