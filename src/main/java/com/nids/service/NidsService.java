package com.nids.service;

import com.nids.model.*;
import com.nids.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NidsService {

    private final DeviceRepository  deviceRepo;
    private final RuleRepository    ruleRepo;
    private final LogRepository     logRepo;
    private final AlertRepository   alertRepo;
    private final ReviewRepository  reviewRepo;

    // --- Devices ---
    public List<NetworkDevice> allDevices()                    { return deviceRepo.findAll(); }
    public void saveDevice(NetworkDevice d)                    { deviceRepo.save(d); }

    // --- Rules ---
    public List<IntrusionRule> allRules()                      { return ruleRepo.findAll(); }
    public void saveRule(IntrusionRule r)                       { ruleRepo.save(r); }

    // --- Logs (trigger fires in DB after this insert) ---
    @Transactional
    public void saveLog(NetworkLog log) {
        logRepo.save(log);
        // DB trigger trg_check_intrusion auto-creates ALERT rows
    }
    public List<NetworkLog> allLogs()                          { return logRepo.findAll(); }

    // --- Alerts ---
    public List<Alert>      allAlerts()                        { return alertRepo.findAll(); }
    public List<Alert>      unreviewedAlerts()                 { return alertRepo.findUnreviewed(); }
    public List<Object[]>   alertsBySeverity()                 { return alertRepo.countBySeverity(); }
    public List<Object[]>   topRules()                         { return alertRepo.topRules(); }
    public List<Object[]>   deviceThreats()                    { return alertRepo.deviceThreats(); }

    // --- Reviews ---
    public void saveReview(AlertReview r)                      { reviewRepo.save(r); }
    public List<AlertReview> allReviews()                      { return reviewRepo.findAll(); }

    // --- Helpers for controllers ---
    public NetworkDevice  getDevice(Integer id)                { return deviceRepo.findById(id).orElseThrow(); }
    public IntrusionRule  getRule(Integer id)                  { return ruleRepo.findById(id).orElseThrow(); }
    public Alert          getAlert(Integer id)                 { return alertRepo.findById(id).orElseThrow(); }
}
