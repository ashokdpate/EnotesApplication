package com.codewitharrays.service;

import java.util.List;

import com.codewitharrays.entity.Category;

public interface CategoryService {
		public boolean saveCategory(Category category);
		public List<Category> getAllCategory();
}
