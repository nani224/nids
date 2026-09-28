package com.nids.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Entity @Table(name = "NETWORK_LOG")
public class NetworkLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private NetworkDevice device;

    @Column(nullable = false)
    private String srcIp;

    @Column(nullable = false)
    private String dstIp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Protocol protocol;

    private Integer port;

    @Column(nullable = false)
    private Integer packets;

    @Column(updatable = false)
    private LocalDateTime logTime = LocalDateTime.now();

    public enum Protocol { TCP, UDP, ICMP }
}
