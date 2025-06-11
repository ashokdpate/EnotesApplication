package com.codewitharrays.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codewitharrays.entity.Notes;
import com.codewitharrays.repository.NotesRepository;

@Component
public class NotesScheduler {

	@Autowired
	private NotesRepository notesRepository;
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotesScheduler() {
		LocalDateTime cuttOfDays = LocalDateTime.now().minusDays(7);
		List<Notes> deletedNotes =	notesRepository.findAllByIsDeleteAndDeletedOnBefore(true,cuttOfDays);
		notesRepository.deleteAll();
	}
	
}
