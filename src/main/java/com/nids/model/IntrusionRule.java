package com.nids.model;

import jakarta.persistence.*;
import lombok.Data;

@Data @Entity @Table(name = "INTRUSION_RULE")
public class IntrusionRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ruleId;

    @Column(nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Protocol protocol;

    private Integer port;

    @Column(nullable = false)
    private Integer threshold;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    public enum Protocol { TCP, UDP, ICMP, ANY }
    public enum Severity  { LOW, MEDIUM, HIGH }
}
