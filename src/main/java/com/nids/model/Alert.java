package com.nids.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Entity @Table(name = "ALERT")
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    private NetworkLog log;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private IntrusionRule rule;

    @Column(updatable = false)
    private LocalDateTime alertTime = LocalDateTime.now();
}
