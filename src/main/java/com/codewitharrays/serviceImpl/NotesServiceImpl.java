package com.codewitharrays.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.entity.Category;
import com.codewitharrays.entity.Notes;
import com.codewitharrays.exception.ResourceNotFoundException;
import com.codewitharrays.repository.CategoryRepository;
import com.codewitharrays.repository.NotesRepository;
import com.codewitharrays.service.NotesService;
@Service
public class NotesServiceImpl implements NotesService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean saveNotes(NotesDTO notesDTO) throws Exception {
		checkCategoryExist(notesDTO.getCategory());
		
		Notes notes = mapper.map(notesDTO, Notes.class);
		Notes saveNotes = notesRepository.save(notes);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
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
