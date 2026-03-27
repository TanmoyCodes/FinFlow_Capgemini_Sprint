package com.finflow.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.finflow.demo.entity.ApplicationStatus;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.repository.ApplicationRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository repository;

    @Mock
    private DocClient docClient;

    @InjectMocks
    private ApplicationService service;

    @Test
    void testApprove() {

        LoanApplication app = new LoanApplication();
        app.setStatus(ApplicationStatus.SUBMITTED);

        when(repository.findById(1L)).thenReturn(Optional.of(app));
        when(repository.save(app)).thenReturn(app);

        LoanApplication result = service.approve(1L);

        assertEquals(ApplicationStatus.APPROVED, result.getStatus());
    }
    @Test
    void testRefreshStatus_shouldNotChangeApproved() {

        LoanApplication app = new LoanApplication();
        app.setStatus(ApplicationStatus.APPROVED);
        app.setUserId("user1");

        when(repository.findById(1L)).thenReturn(Optional.of(app));

        LoanApplication result = service.refreshStatus(1L);

        // ✅ should remain APPROVED
        assertEquals(ApplicationStatus.APPROVED, result.getStatus());

        // ❌ should NOT save again
        verify(repository, never()).save(app);
    }
    @Test
    void testRefreshStatus_verified() {

        LoanApplication app = new LoanApplication();
        app.setStatus(ApplicationStatus.SUBMITTED);
        app.setUserId("user1");

        when(repository.findById(1L)).thenReturn(Optional.of(app));
        when(repository.save(app)).thenReturn(app); // 🔥 THIS WAS MISSING

        when(docClient.getDocStatus(anyString(), anyString()))
                .thenReturn("VERIFIED");

        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getCredentials()).thenReturn("dummy-token");

        SecurityContextHolder.setContext(context);

        LoanApplication result = service.refreshStatus(1L);

        assertEquals(ApplicationStatus.DOCS_VERIFIED, result.getStatus());
    }
    @Test
    void testSubmitApplication_verified() {

        LoanApplication app = new LoanApplication();
        app.setUserId("user1");

        // 🔥 first save (after SUBMITTED)
        when(repository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(docClient.getDocStatus(anyString(), anyString()))
                .thenReturn("VERIFIED");

        // 🔥 SecurityContext mock
        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getCredentials()).thenReturn("dummy-token");

        SecurityContextHolder.setContext(context);

        LoanApplication result = service.submitApplication(app);

        assertEquals(ApplicationStatus.DOCS_VERIFIED, result.getStatus());

        // verify save called twice
        verify(repository, times(2)).save(any(LoanApplication.class));
    }
    @Test
    void testSubmitApplication_pending() {

        LoanApplication app = new LoanApplication();
        app.setUserId("user1");

        when(repository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(docClient.getDocStatus(anyString(), anyString()))
                .thenReturn("PENDING");

        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getCredentials()).thenReturn("dummy-token");

        SecurityContextHolder.setContext(context);

        LoanApplication result = service.submitApplication(app);

        assertEquals(ApplicationStatus.DOCS_PENDING, result.getStatus());

        verify(repository, times(2)).save(any(LoanApplication.class));
    }
}