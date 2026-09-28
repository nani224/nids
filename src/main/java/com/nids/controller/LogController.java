package com.nids.controller;

import com.nids.model.NetworkLog;
import com.nids.service.NidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final NidsService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("logs", svc.allLogs());
        m.addAttribute("log", new NetworkLog());
        m.addAttribute("devices", svc.allDevices());
        return "logs";
    }

    @PostMapping
    public String save(@ModelAttribute NetworkLog log,
                       @RequestParam Integer deviceId) {
        log.setDevice(svc.getDevice(deviceId));
        svc.saveLog(log);
        return "redirect:/logs";
    }
}
