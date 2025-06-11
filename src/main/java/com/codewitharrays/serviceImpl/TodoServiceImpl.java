package com.codewitharrays.serviceImpl;

import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.codewitharrays.dto.TodoDTO;
import com.codewitharrays.dto.TodoDTO.StatusDTO;
import com.codewitharrays.entity.Todo;
import com.codewitharrays.enums.TodoStatus;
import com.codewitharrays.exception.ResourceNotFoundException;
import com.codewitharrays.repository.TodoRepository;
import com.codewitharrays.service.TodoService;

@Service
public class TodoServiceImpl implements TodoService{

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TodoRepository todoRepository;
	
	
	@Override
	public Boolean saveTodo(TodoDTO todoDTO) throws Exception {
		
		Todo todo = mapper.map(todoDTO, Todo.class);
		todo.setStatusId(todoDTO.getStatus().getId());
		Todo saveTodo = todoRepository.save(todo);
		if (!ObjectUtils.isEmpty(saveTodo)) {
			return true;
		}
		
		return false;
	}

	@Override
	public TodoDTO getTodoById(Integer id) throws Exception {
			Todo todo = todoRepository.findById(id).orElseThrow(()->
			new ResourceNotFoundException("Todo not found"));
		
			TodoDTO todoDTO = mapper.map(todo, TodoDTO.class);
			setStatus(todoDTO, todo);
			return todoDTO;
	}

	private void setStatus(TodoDTO todoDTO, Todo todo) {
		for(TodoStatus st:TodoStatus.values()) {
			if (st.getId().equals(todo.getStatusId())) {
				StatusDTO status = StatusDTO.builder()
				.id(st.getId())
				.name(st.getName())
				.build();
				
				todoDTO.setStatus(status);
			}
		}
		
	}

	@Override
	public List<TodoDTO> getTodoByUser() {
		Integer userId = 2;
		List<Todo> todos = todoRepository.findByCreatedBy(userId);
	return	todos.stream().map(td->mapper.map(td,TodoDTO.class)).toList();
	}

}
