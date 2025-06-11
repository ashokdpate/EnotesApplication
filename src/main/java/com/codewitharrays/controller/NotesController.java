package com.codewitharrays.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codewitharrays.dto.FavouriteNoteDTO;
import com.codewitharrays.dto.NotesDTO;
import com.codewitharrays.dto.NotesResponse;
import com.codewitharrays.entity.FileDetails;
import com.codewitharrays.service.NotesService;
import com.codewitharrays.utils.CommonUtils;

@RestController
@RequestMapping("api/v1/notes")
public class NotesController {

	
		@Autowired
		private NotesService notesService;

		@PostMapping("/save")
		public ResponseEntity<?> saveNotes(@RequestParam String notes,@RequestParam(required = false) MultipartFile file) throws Exception{
			Boolean saveNotes = notesService.saveNotes(notes,file);
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
		
		//http://localhost:8080/api/v1/notes/download/1  
		//directly paste the id and url the file is downloaded.
		@GetMapping("download/{id}")
		public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
			FileDetails fileDetails = notesService.getFileDetails(id);
			byte[] data = notesService.downloadFile(fileDetails);

			HttpHeaders headers = new HttpHeaders();
			String contentType = CommonUtils.getContentType(fileDetails.getOriginalFileName());
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

			return ResponseEntity.ok().headers(headers).body(data);
		}
		
		
		@GetMapping("/pagination/user-notes")
		public ResponseEntity<?> paginationAllNotesByUser(@RequestParam(name = "pageNo" ,defaultValue = "0")Integer pageNo, 
														  @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize ){
				Integer userId=1;
			NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
			
			return new ResponseEntity<>(notes,HttpStatus.OK);
		}
		
		@DeleteMapping("/soft-delete/{id}")
		public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception{
			notesService.softDeleteNotes(id);
			return new ResponseEntity<>("Delete success",HttpStatus.OK);
		} 
		
		@PutMapping("/restore/{id}")
		public ResponseEntity<?> restoreNotes(@PathVariable Integer id)throws Exception {
			notesService.restoreNotes(id);
			return new ResponseEntity<>("Restore success",HttpStatus.OK);
		}
		
		@GetMapping("/recycle-bin")
		public ResponseEntity<?> getUserRecycleBinNotes(){
			Integer userId=1;
			List<NotesDTO> notes = notesService.getuserRecycleBinNotes(userId);
			if(CollectionUtils.isEmpty(notes))
			{
				return ResponseEntity.noContent().build();
			}
			else {
				return new ResponseEntity<>(notes,HttpStatus.OK);
			}
		}	
		
		@DeleteMapping("/hard-delete/{id}")
		public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception{
			notesService.hardDeleteNotes(id);
			return new ResponseEntity<>("Delete success",HttpStatus.OK);
		}
		
		@DeleteMapping("recycle-bin/deleteAll")
		public ResponseEntity<?> emptyRecycleBin() {
			Integer userId=1;
			notesService.emptyRecyleBin(userId);
			return new ResponseEntity<>("Delete success",HttpStatus.OK);
		}
		
		
		@PostMapping("/fav/{noteId}")
		public ResponseEntity<?> favouriteNotes (@PathVariable Integer noteId) throws Exception{
				notesService.favouriteNotes(noteId);
			return new ResponseEntity<>("favourite note added success",HttpStatus.CREATED);
		}
		
		@DeleteMapping("/un-fav/{favNoteId}")
		public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId ) throws Exception{
			notesService.unFavouriteNotes(favNoteId);
			return new ResponseEntity<>("Remove favourite",HttpStatus.OK);
		}
		
		@GetMapping("/fav-notes")
		public ResponseEntity<?> getUserFavouriteNotes() throws Exception{
			List<FavouriteNoteDTO> userFavouriteNotes = notesService.getUserFavouriteNotes();
			if (CollectionUtils.isEmpty(userFavouriteNotes)) {
				return ResponseEntity.noContent().build();
			}
			return new ResponseEntity<>(userFavouriteNotes,HttpStatus.OK);
		}
}
