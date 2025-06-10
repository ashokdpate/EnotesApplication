package com.codewitharrays.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.codewitharrays.entity.FileDetails;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, Integer>{

}
