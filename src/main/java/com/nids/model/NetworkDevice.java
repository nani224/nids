package com.nids.model;

import jakarta.persistence.*;
import lombok.Data;

@Data @Entity @Table(name = "NETWORK_DEVICE")
public class NetworkDevice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deviceId;

    @Column(nullable = false)
    private String deviceName;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType deviceType;

    private String location;

    public enum DeviceType { PC, Router, Server, Switch }
}
