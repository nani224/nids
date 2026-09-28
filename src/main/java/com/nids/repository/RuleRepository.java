package com.nids.repository;

import com.nids.model.IntrusionRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<IntrusionRule, Integer> {}
