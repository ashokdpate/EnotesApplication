package com.codewitharrays.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.CategoryResponse;
import com.codewitharrays.entity.Category;
import com.codewitharrays.service.CategoryService;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDto){
		boolean status = categoryService.saveCategory(categoryDto);
		if (status) {
			return new ResponseEntity<>("save success",HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>("failed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all-categories")
	public ResponseEntity<?> getAllCategory(){
		List<CategoryDTO> allCategory = categoryService.getAllCategory();
		
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}else {
			return new ResponseEntity<>(allCategory,HttpStatus.OK);
		}
	}
	
	@GetMapping("/active-categories")
	public ResponseEntity<?> getActiveCategory(){
		List<CategoryResponse> activeCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(activeCategory)) {
			return ResponseEntity.noContent().build();
		}else {
			return new ResponseEntity<>(activeCategory,HttpStatus.OK);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id){
			CategoryDTO categoryDto = categoryService.getCategoryById(id);
			if (ObjectUtils.isEmpty(categoryDto)) {
				return new ResponseEntity<>("category not found id:"+id, HttpStatus.NOT_FOUND);
			}
			else {
				return new ResponseEntity<>(categoryDto,HttpStatus.OK);
			}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
		Boolean response = categoryService.deleteCategoryById(id);
		if (response) {
			return new ResponseEntity<>("category deleted successfully",HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Category Not deleted",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
