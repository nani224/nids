package com.nids.repository;

import com.nids.model.NetworkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<NetworkLog, Integer> {}
