package com.codewitharrays.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import lombok.Setter;

@Builder
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Todo extends BaseModel{

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
	
		private String title;
		
		@Column(name = "status")
		private Integer statusId;
}
