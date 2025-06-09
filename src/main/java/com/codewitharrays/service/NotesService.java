package com.codewitharrays.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.entity.FileDetails;

public interface NotesService {

	public Boolean saveNotes(String notes,MultipartFile file) throws Exception;
	
	public List<NotesDTO> getAllNotes();

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;
	
}
