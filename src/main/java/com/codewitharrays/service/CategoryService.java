package com.codewitharrays.service;

import java.util.List;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.CategoryResponse;
import com.codewitharrays.entity.Category;

public interface CategoryService {
		public boolean saveCategory(CategoryDTO categoryDto);
		public List<CategoryDTO> getAllCategory();
		
		public List<CategoryResponse> getActiveCategory();
}
