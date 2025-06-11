package com.codewitharrays.service;

import java.util.List;

import com.codewitharrays.dto.TodoDTO;

public interface TodoService {
		
		public Boolean saveTodo(TodoDTO todoDTO) throws Exception;

		public TodoDTO getTodoById(Integer id) throws Exception;
		
		public List<TodoDTO> getTodoByUser();
}
