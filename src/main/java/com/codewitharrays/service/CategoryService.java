package com.codewitharrays.service;

import java.util.List;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.CategoryResponse;


public interface CategoryService {
		public Boolean saveCategory(CategoryDTO categoryDto);
		public List<CategoryDTO> getAllCategory();
		
		public List<CategoryResponse> getActiveCategory();
		
		public CategoryDTO getCategoryById(Integer id);
		public Boolean deleteCategoryById(Integer id);
}
