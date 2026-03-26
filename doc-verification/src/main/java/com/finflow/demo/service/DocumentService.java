package com.finflow.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finflow.demo.entity.Document;
import com.finflow.demo.enums.DocumentStatus;
import com.finflow.demo.repository.DocumentRepository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public void upload(MultipartFile file, String userId) {

        try {
            System.out.println("📥 Upload started");
            System.out.println("👤 UserId: " + userId);

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // ✅ Folder setup
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File folder = new File(uploadDir);

            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                System.out.println("📁 Folder created: " + created);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String fullPath = uploadDir + filename;

            System.out.println("📂 Saving to: " + fullPath);

            file.transferTo(new File(fullPath));

            System.out.println("✅ File saved successfully");

            // 🔥 CHECK EXISTING DOCUMENT
            Optional<Document> existingDoc = repository.findByUserId(userId);

            Document doc;

            if (existingDoc.isPresent()) {
                // 🔁 UPDATE existing document
                doc = existingDoc.get();
                System.out.println("♻️ Updating existing document");

                // (Optional) delete old file
                if (doc.getFilePath() != null) {
                    File oldFile = new File(doc.getFilePath());
                    if (oldFile.exists()) {
                        oldFile.delete();
                        System.out.println("🗑️ Old file deleted");
                    }
                }

            } else {
                // 🆕 CREATE new document
                doc = new Document();
                doc.setUserId(userId);
                System.out.println("🆕 Creating new document");
            }

            // ✅ Update fields
            doc.setFilePath(fullPath);
            doc.setStatus(DocumentStatus.PENDING);

            // 💾 Save (insert OR update)
            repository.save(doc);

            System.out.println("💾 Saved in DB");

        } catch (Exception e) {
            System.out.println("❌ REAL ERROR BELOW 👇");
            e.printStackTrace();
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    public String getStatus(String userId) {

        System.out.println("🔍 Checking document for user: " + userId);

        if (userId == null || userId.trim().isEmpty()) {
            return "INVALID_USER";
        }

        return repository.findByUserId(userId)
                .map(doc -> doc.getStatus().name())
                .orElse("NOT_UPLOADED");
    }
    
    public List<Document> getAllDocuments() {
        return repository.findAll();
    }
    
    public String verifyDocument(String userId) {

        Optional<Document> docOpt = repository.findByUserId(userId);

        if (docOpt.isEmpty()) {
            return "DOCUMENT_NOT_FOUND";
        }

        Document doc = docOpt.get();
        doc.setStatus(DocumentStatus.VERIFIED);

        repository.save(doc);

        return "DOCUMENT_VERIFIED";
    }
    
    public String rejectDocument(String userId) {

        Optional<Document> docOpt = repository.findByUserId(userId);

        if (docOpt.isEmpty()) {
            return "DOCUMENT_NOT_FOUND";
        }

        Document doc = docOpt.get();
        doc.setStatus(DocumentStatus.REJECTED);

        repository.save(doc);

        return "DOCUMENT_REJECTED";
    }
}