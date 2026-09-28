package com.nids.repository;

import com.nids.model.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<NetworkDevice, Integer> {}
