package com.finflow.demo.LoanApplicationDTO;
public class LoanApplicationDTO {
    private Long id;
    private String userId;
    private String fullName;
    private String status;

    public LoanApplicationDTO(Long id, String userId, String fullName, String status) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getStatus() { return status; }
}
