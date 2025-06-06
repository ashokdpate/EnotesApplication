package com.codewitharrays.serviceImpl;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.codewitharrays.entity.Category;
import com.codewitharrays.repository.CategoryRepository;
import com.codewitharrays.service.CategoryService;

@Service
public class CategoryServiceImpl  implements CategoryService{

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public boolean saveCategory(Category category) {
			category.setIsDelete(false);
			category.setIsActive(true);
			category.setCreatedBy(1);
			category.setCreatedDate(new Date());
			Category status = categoryRepository.save(category);
			if (ObjectUtils.isEmpty(status)) {
				return false;
			}
			
			return true;
	}

	@Override
	public List<Category> getAllCategory() {
			List<Category> allCategories = categoryRepository.findAll();
			return allCategories;
	}

}
