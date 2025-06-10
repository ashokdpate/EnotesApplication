package com.codewitharrays.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codewitharrays.entity.Notes;
@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer>{

	Page<Notes> findByCreatedBy(Integer userId, PageRequest pageable);

	List<Notes> findByCreatedByAndIsDeleteTrue(Integer userId);

	Page<Notes> findByCreatedByAndIsDeleteFalse(Integer userId, PageRequest pageable);

	List<Notes> findAllByIsDeleteAndDeletedOnBefore(boolean b, LocalDateTime cuttOfDays);

	
}
