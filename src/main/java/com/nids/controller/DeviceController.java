package com.nids.controller;

import com.nids.model.NetworkDevice;
import com.nids.service.NidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final NidsService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("devices", svc.allDevices());
        m.addAttribute("device", new NetworkDevice());
        return "devices";
    }

    @PostMapping
    public String save(@ModelAttribute NetworkDevice d) {
        svc.saveDevice(d);
        return "redirect:/devices";
    }
}
