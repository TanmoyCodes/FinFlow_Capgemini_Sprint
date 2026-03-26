package com.finflow.demo.dto;
public class AdminStatsDTO {

    private long totalUsers;
    private long totalApplications;
    private long approved;
    private long rejected;
    private long pending;
    private long docPending;
    private long docVerified;

    public AdminStatsDTO(long totalUsers, long totalApplications,
                         long approved, long rejected, long pending,
                         long docPending, long docVerified) {
        this.totalUsers = totalUsers;
        this.totalApplications = totalApplications;
        this.approved = approved;
        this.rejected = rejected;
        this.pending = pending;
        this.docPending = docPending;
        this.docVerified = docVerified;
    }

    public long getTotalUsers() { return totalUsers; }
    public long getTotalApplications() { return totalApplications; }
    public long getApproved() { return approved; }
    public long getRejected() { return rejected; }
    public long getPending() { return pending; }
    public long getDocPending() { return docPending; }
    public long getDocVerified() { return docVerified; }
}