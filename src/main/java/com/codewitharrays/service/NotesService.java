package com.codewitharrays.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codewitharrays.dto.NotesDTO;

public interface NotesService {

	public Boolean saveNotes(String notes,MultipartFile file) throws Exception;
	
	public List<NotesDTO> getAllNotes();
}
