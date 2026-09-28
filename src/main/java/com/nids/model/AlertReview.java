package com.nids.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Entity @Table(name = "ALERT_REVIEW")
@IdClass(AlertReviewId.class)
public class AlertReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Id
    @ManyToOne
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @Column(nullable = false)
    private String analyst;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String remarks;

    @Column(updatable = false)
    private LocalDateTime reviewTime = LocalDateTime.now();

    public enum Status { CONFIRMED, FALSE_POSITIVE, UNDER_REVIEW }
}
