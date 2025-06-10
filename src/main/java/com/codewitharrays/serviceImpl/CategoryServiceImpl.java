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
import com.codewitharrays.exception.ExistDataException;
import com.codewitharrays.exception.ResourceNotFoundException;
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
			Boolean exsit=	categoryRepository.existsByName(categoryDto.getName().trim());
			if (exsit) {
				throw  new ExistDataException("category already exist");
			}
			Category category = mapper.map(categoryDto, Category.class);
			
			if(ObjectUtils.isEmpty(category.getId())) {
				category.setIsDelete(false);
				
				category.setCreatedDate(new Date());
			}else {
				updateCategory(category);
			}
			

			Category status = categoryRepository.save(category);
			if (ObjectUtils.isEmpty(status)) {
				return false;
			}
			
			return true;
	}

	private void updateCategory(Category category) {
					Optional<Category> optional = categoryRepository.findById(category.getId());
					if (optional.isPresent()) {
						Category existCategory = optional.get();			
						category.setIsDelete(existCategory.getIsDelete());
						category.setCreatedBy(existCategory.getCreatedBy());
						category.setCreatedDate(existCategory.getCreatedDate());
						
						
					}
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
	public CategoryDTO getCategoryById(Integer id) throws Exception {
			 Category category = categoryRepository.findByIdAndIsDeleteFalse(id)
					.orElseThrow(()-> new ResourceNotFoundException("Category not found with id="+id));
			if (!ObjectUtils.isEmpty(category)) {
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
