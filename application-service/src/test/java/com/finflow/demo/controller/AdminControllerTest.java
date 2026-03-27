package com.finflow.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.finflow.demo.LoanApplicationDTO.LoanApplicationDTO;
import com.finflow.demo.dto.AdminStatsDTO;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.security.JwtFilter;
import com.finflow.demo.service.ApplicationService;

@WebMvcTest(
	    controllers = AdminController.class,
	    excludeAutoConfiguration = {
	        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
	    }
	)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService service;
    
    
    @MockBean   // 🔥 THIS FIXES YOUR ERROR
    private JwtFilter jwtFilter;
    
    @Test
    @WithMockUser(roles = "ADMIN")  // 🔥 IMPORTANT
    void testGetAllApplications() throws Exception {

        LoanApplication app = new LoanApplication();
        app.setUserId("user1");

        LoanApplicationDTO dto = new LoanApplicationDTO(1L, "user1", "Tanmoy", "APPROVED");

        when(service.getAllApplications()).thenReturn(List.of(dto));

        mockMvc.perform(get("/application/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }
    

@Test
@WithMockUser(roles = "ADMIN")
void testApproveApplication() throws Exception {

    LoanApplication app = new LoanApplication();
    app.setUserId("user1");

    when(service.approve(1L)).thenReturn(app);

    mockMvc.perform(post("/application/admin/1/approve"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value("user1"));
}


@Test
@WithMockUser(roles = "ADMIN")
void testRejectApplication() throws Exception {

    LoanApplication app = new LoanApplication();
    app.setUserId("user2");

    when(service.reject(2L)).thenReturn(app);

    mockMvc.perform(post("/application/admin/2/reject"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value("user2"));
}



@Test
@WithMockUser(roles = "ADMIN")
void testGetStats() throws Exception {

    AdminStatsDTO stats = new AdminStatsDTO(
            10,  // totalUsers
            20,  // totalApplications
            5,   // approved
            3,   // rejected
            12,  // pending
            0,
            0
    );

    when(service.getAdminStats()).thenReturn(stats);

    mockMvc.perform(get("/application/admin/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalApplications").value(20))
            .andExpect(jsonPath("$.approved").value(5));
}
@Test
@WithMockUser(roles = "ADMIN")
void testApproveApplication_notFound() throws Exception {

    // 🔥 simulate error
    when(service.approve(1L))
            .thenThrow(new RuntimeException("Application not found"));

    mockMvc.perform(post("/application/admin/1/approve"))
            .andExpect(status().isBadRequest())   // 👈 important
            .andExpect(content().string("Application not found"));
}
}