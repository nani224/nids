package com.nids.controller;

import com.nids.service.NidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final NidsService svc;

    @GetMapping("/")
    public String dashboard(Model m) {
        m.addAttribute("deviceCount",  svc.allDevices().size());
        m.addAttribute("ruleCount",    svc.allRules().size());
        m.addAttribute("logCount",     svc.allLogs().size());
        m.addAttribute("alertCount",   svc.allAlerts().size());
        m.addAttribute("recentAlerts", svc.allAlerts().stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/queries")
    public String queryDashboard(Model m) {
        m.addAttribute("bySeverity",    svc.alertsBySeverity());
        m.addAttribute("topRules",      svc.topRules());
        m.addAttribute("deviceThreats", svc.deviceThreats());
        m.addAttribute("unreviewed",    svc.unreviewedAlerts());
        return "queries";
    }
}
