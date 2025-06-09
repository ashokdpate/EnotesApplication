package com.codewitharrays.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.entity.Category;
import com.codewitharrays.entity.FileDetails;
import com.codewitharrays.entity.Notes;
import com.codewitharrays.exception.ResourceNotFoundException;
import com.codewitharrays.repository.CategoryRepository;
import com.codewitharrays.repository.FileRepository;
import com.codewitharrays.repository.NotesRepository;
import com.codewitharrays.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class NotesServiceImpl implements NotesService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Value("${file.upload.path}")
	private String uploadpath;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Override
	public Boolean saveNotes(String notes,MultipartFile file) throws Exception {
		
		ObjectMapper obj= new ObjectMapper();
		NotesDTO notesDTO = obj.readValue(notes, NotesDTO.class);
		
		
		
		checkCategoryExist(notesDTO.getCategory());
		
		Notes notesMap = mapper.map(notesDTO, Notes.class);
		
				FileDetails fileDetails=saveFileDetails(file);
				if (!ObjectUtils.isEmpty(fileDetails)) {
					notesMap.setFileDetails(fileDetails);
				}	
				else {
					notesMap.setFileDetails(null);
				}
		
		
		Notes saveNotes = notesRepository.save(notesMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			
			FileDetails fileDetails= new FileDetails();
			String originalFileName = file.getOriginalFilename();
			
			fileDetails.setOriginalFileName(originalFileName);
			//to get display name creating method and pass original file
			fileDetails.setDisplayFileName(getDisplayName(originalFileName));
			
			//generating the random number
			String rndString=UUID.randomUUID().toString();
			
			//retrive extension name
			String extension = FilenameUtils.getExtension(originalFileName);
			
			//giving uploadfile name with random number 
			String uploadFileName=rndString+"."+extension;
			fileDetails.setUploadFileName(uploadFileName);
			
			//fileSize setting
			fileDetails.setFileSize(file.getSize());
			
			//create file object with path
			File saveFile=new File(uploadpath);
			//if not exist then create folder
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}
			//store file to the path location
			String storePath = uploadpath.concat(uploadFileName);
			//setting this storepath to path
			fileDetails.setPath(storePath);
			
			//upload in the local store
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if (upload!=0) {
				FileDetails save = fileRepository.save(fileDetails);
				return save;
			}
			
		}
		
		return null;
	}

	private String getDisplayName(String originalFileName) {
		//here we can get the file extension
		String extension = FilenameUtils.getExtension(originalFileName);
		//here removing extension so i get the name only
		String fileName = FilenameUtils.removeExtension(originalFileName);
		
		//here we can shorter file name upto 8 char using substring
		if (fileName.length()>8) {
			fileName = fileName.substring(0, 7);
		}
		fileName=fileName + "." + extension;
		return fileName;
	}

	private void checkCategoryExist(NotesDTO.CategoryDTO category) throws Exception{
		categoryRepository.findById(category.getId())
		.orElseThrow(()-> new ResourceNotFoundException("category id invalid"));
		
	}

	@Override
	public List<NotesDTO> getAllNotes() {
		List<Notes> allNotes = notesRepository.findAll();
		List<NotesDTO> list = allNotes.stream().map(notes-> mapper.map(notes, NotesDTO.class)).toList();
		return list;
	}

}
