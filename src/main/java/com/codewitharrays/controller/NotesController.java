package com.codewitharrays.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.service.NotesService;

@RestController
@RequestMapping("api/v1/notes")
public class NotesController {

	
		@Autowired
		private NotesService notesService;

		@PostMapping("/save")
		public ResponseEntity<?> saveNotes(@RequestBody NotesDTO notesDTO) throws Exception{
			Boolean saveNotes = notesService.saveNotes(notesDTO);
			if (saveNotes) {
				return new ResponseEntity<>("notes saved",HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>("Notes save failed",HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@GetMapping("/all")
		public ResponseEntity<?> getAllNotes(){
			List<NotesDTO> allNotes = notesService.getAllNotes();
			if (CollectionUtils.isEmpty(allNotes)) {
				return ResponseEntity.noContent().build();
			}
			else {
				return new ResponseEntity<>(allNotes,HttpStatus.OK);
			}
		
		}

}
