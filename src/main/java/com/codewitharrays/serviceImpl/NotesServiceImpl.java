package com.codewitharrays.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.codewitharrays.dto.FavouriteNoteDTO;
import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.dto.NotesDTO.FileDto;
import com.codewitharrays.dto.NotesResponse;
import com.codewitharrays.entity.FavouriteNote;
import com.codewitharrays.entity.FileDetails;
import com.codewitharrays.entity.Notes;
import com.codewitharrays.exception.ResourceNotFoundException;
import com.codewitharrays.repository.CategoryRepository;
import com.codewitharrays.repository.FavouriteNoteRepository;
import com.codewitharrays.repository.FileRepository;
import com.codewitharrays.repository.NotesRepository;
import com.codewitharrays.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class NotesServiceImpl implements NotesService{

	
	@Autowired
	private FavouriteNoteRepository favouriteNoteRepository;
	
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
		notesDTO.setIsDelete(false);
		notesDTO.setDeletedOn(null);
		
		
		checkCategoryExist(notesDTO.getCategory());
		
		Notes notesMap = mapper.map(notesDTO, Notes.class);
		
				FileDetails fileDetails=saveFileDetails(file);
				if (!ObjectUtils.isEmpty(fileDetails)) {
					notesMap.setFileDetails(fileDetails);
				}	
				else {
					if (ObjectUtils.isEmpty(notesDTO.getId())) {
						notesMap.setFileDetails(null);
					}
					
				}
		
		
		Notes saveNotes = notesRepository.save(notesMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}
	
	private void updateNotes(NotesDTO notesDto, MultipartFile file) throws Exception {

		Notes existNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));

		// user not choose any file at update time
		if (ObjectUtils.isEmpty(file)) {
				notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FileDto.class));
			
		}

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
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {

		InputStream io = new FileInputStream(fileDetails.getPath());

		return StreamUtils.copyToByteArray(io);
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		return fileDtls;
	}

	@Override
	public List<NotesDTO> getAllNotes() {
		List<Notes> allNotes = notesRepository.findAll();
		List<NotesDTO> list = allNotes.stream().map(notes-> mapper.map(notes, NotesDTO.class)).toList();
		return list;
	}
	
	public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo,Integer pageSize) {
		
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes =notesRepository.findByCreatedByAndIsDeleteFalse(userId,pageable);
	
		List<NotesDTO> notesDTO = pageNotes.get().map(n->mapper.map(n, NotesDTO.class)).toList();
		
		NotesResponse notes = NotesResponse.builder().notes(notesDTO)
		.pageNo(pageNotes.getNumber())
		.pageSize(pageNotes.getSize())
		.totalElements(pageNotes.getTotalElements())
		.totalPages(pageNotes.getTotalPages())
		.isFirst(pageNotes.isFirst())
		.isLast(pageNotes.isLast())
		.build();
		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes id is invalid"));
		notes.setIsDelete(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes restoreNotes = notesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("notes not available in recyle bin"));
		restoreNotes.setIsDelete(false);
		restoreNotes.setDeletedOn(null);
		notesRepository.save(restoreNotes);
	}

	@Override
	public List<NotesDTO> getuserRecycleBinNotes(Integer userId) {
		
		//notes is created by user and isdelete true only this data get
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeleteTrue(userId);
		List<NotesDTO> list = recycleNotes.stream().map(note->mapper.map(recycleNotes, NotesDTO.class)).toList();
		return list;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes not found"));
		if (notes.getIsDelete()) {
			notesRepository.delete(notes);
		}
		else {
			throw new IllegalArgumentException("Sorry you cant hard delete directly");
		}
	}

	@Override
	public void emptyRecyleBin(Integer userId) {
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeleteTrue(userId);
		if (!CollectionUtils.isEmpty(recycleNotes)) {
			notesRepository.deleteAll(recycleNotes);
		}
	}

	@Override
	public void favouriteNotes(Integer noteId) throws Exception {
		Integer userId=1;
		Notes notes = notesRepository.findById(noteId).orElseThrow(()->
		new ResourceNotFoundException("Notes id not found"));
		
		FavouriteNote favNote = FavouriteNote.builder()
		.notes(notes)
		.userId(userId)
		.build();
		favouriteNoteRepository.save(favNote);
		
	}

	@Override
	public void unFavouriteNotes(Integer favNoteId) throws Exception {
		FavouriteNote favNote = favouriteNoteRepository.findById(favNoteId).orElseThrow(()-> new ResourceNotFoundException("Fav note id note found"));
		favouriteNoteRepository.delete(favNote);
	}

	@Override
	public List<FavouriteNoteDTO> getUserFavouriteNotes() throws Exception {
		Integer userId=1;
		List<FavouriteNote> favNotes = favouriteNoteRepository.findByUserId(userId);
	return favNotes.stream().map(fav->mapper.map(fav, FavouriteNoteDTO.class)).toList();
	
	}
	
	
	
	
	

}
