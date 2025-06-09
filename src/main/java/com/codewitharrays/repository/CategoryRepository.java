package com.codewitharrays.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codewitharrays.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

	

	Optional<Category> findByIdAndIsDeleteFalse(Integer id);

	List<Category> findByIsDeleteFalse();

	List<Category> findByIsActiveTrueAndIsDeleteFalse();

	Boolean existsByName(String name);

}
