package com.codewitharrays.serviceImpl;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.codewitharrays.dto.CategoryDTO;
import com.codewitharrays.dto.CategoryResponse;
import com.codewitharrays.entity.Category;
import com.codewitharrays.repository.CategoryRepository;
import com.codewitharrays.service.CategoryService;



@Service
public class CategoryServiceImpl  implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean saveCategory(CategoryDTO categoryDto) {
			
//		Without model mapper
//			Category category=new Category();
//			category.setName(categoryDto.getName());
//			category.setDescription(categoryDto.getDescription());
//			category.setIsActive(categoryDto.getIsActive());
		
//		With model  mapper in one line
		
			Category category = mapper.map(categoryDto, Category.class);
			
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
	public List<CategoryDTO> getAllCategory() {
			List<Category> allCategories = categoryRepository.findByIsDeleteFalse();
			List<CategoryDTO> categoryDtoList = 
					allCategories.stream().map(e->mapper.map(e, CategoryDTO.class))
										  .toList();
			return categoryDtoList;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeleteFalse();
		List<CategoryResponse> categoryResponses = categories.stream().map(e->mapper.map(e, CategoryResponse.class)).toList();
		return categoryResponses;
	}

	@Override
	public CategoryDTO getCategoryById(Integer id) {
			Optional<Category> optional = categoryRepository.findByIdAndIsDeleteFalse(id);
			if (optional.isPresent()) {
					Category category = optional.get();
				return mapper.map(category, CategoryDTO.class);
			}
			return null;
	}

	@Override
	public Boolean deleteCategoryById(Integer id) {
		Optional<Category> optional = categoryRepository.findById(id);
		if (optional.isPresent()) {
			Category category = optional.get();
			category.setIsDelete(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
