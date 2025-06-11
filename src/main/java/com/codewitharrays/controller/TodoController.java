package com.codewitharrays.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewitharrays.dto.TodoDTO;
import com.codewitharrays.service.TodoService;

@RestController
@RequestMapping("api/v1/todo")
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	@PostMapping("/")
	public ResponseEntity<?> saveTodo(@RequestBody TodoDTO todoDTO) throws Exception {
		Boolean saveTodo = todoService.saveTodo(todoDTO);
		if (saveTodo) {
			return new ResponseEntity<>("Todo saved",HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Todo saved failed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> saveTodo(@PathVariable Integer id) throws Exception {
		 TodoDTO todo = todoService.getTodoById(id);
		return new ResponseEntity<>(todo,HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllTodoByUser() throws Exception {
		List<TodoDTO> todoList = todoService.getTodoByUser();
		if (CollectionUtils.isEmpty(todoList)) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(todoList, HttpStatus.OK);
	}
}
