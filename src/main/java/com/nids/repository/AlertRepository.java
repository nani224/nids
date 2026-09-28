package com.nids.repository;

import com.nids.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

    // Alerts with no review yet (subquery demo)
    @Query("SELECT a FROM Alert a WHERE a.alertId NOT IN (SELECT r.alert.alertId FROM AlertReview r)")
    List<Alert> findUnreviewed();

    // Count by severity (GROUP BY demo)
    @Query("SELECT a.rule.severity, COUNT(a) FROM Alert a GROUP BY a.rule.severity ORDER BY a.rule.severity")
    List<Object[]> countBySeverity();

    // Top triggered rules (ORDER BY + aggregation)
    @Query("SELECT a.rule.ruleName, COUNT(a) as cnt FROM Alert a GROUP BY a.rule ORDER BY cnt DESC")
    List<Object[]> topRules();

    // Device threat count (multi-join)
    @Query("SELECT d.deviceName, d.ipAddress, COUNT(a) as cnt FROM Alert a JOIN a.log l JOIN l.device d GROUP BY d ORDER BY cnt DESC")
    List<Object[]> deviceThreats();
}
