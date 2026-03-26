package com.finflow.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.finflow.demo.entity.Document;
import com.finflow.demo.enums.DocumentStatus;

import java.util.Optional;
public interface DocumentRepository extends JpaRepository<Document, Long> {

	Optional<Document> findByUserId(String userId);
	//hello
	long countByStatus(DocumentStatus status);
}
