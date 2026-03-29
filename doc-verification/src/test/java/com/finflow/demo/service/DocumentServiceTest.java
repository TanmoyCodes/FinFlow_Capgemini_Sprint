package com.finflow.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.finflow.demo.entity.Document;
import com.finflow.demo.enums.DocumentStatus;
import com.finflow.demo.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @InjectMocks
    private DocumentService service;

    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document();
        document.setUserId("user123");
        document.setStatus(DocumentStatus.PENDING);
    }

    @Test
    void testGetStatus_WhenDocumentExists() {
        when(repository.findByUserId("user123")).thenReturn(Optional.of(document));

        String status = service.getStatus("user123");

        assertEquals("PENDING", status);
        verify(repository, times(1)).findByUserId("user123");
    }

    @Test
    void testGetStatus_WhenDocumentDoesNotExist() {
        when(repository.findByUserId("notfound")).thenReturn(Optional.empty());

        String status = service.getStatus("notfound");

        assertEquals("NOT_UPLOADED", status);
        verify(repository, times(1)).findByUserId("notfound");
    }

    @Test
    void testVerifyDocument_Success() {
        when(repository.findByUserId("user123")).thenReturn(Optional.of(document));

        String result = service.verifyDocument("user123");

        assertEquals("DOCUMENT_VERIFIED", result);
        assertEquals(DocumentStatus.VERIFIED, document.getStatus());
        verify(repository, times(1)).save(document);
    }
}
