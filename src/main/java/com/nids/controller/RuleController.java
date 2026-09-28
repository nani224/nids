package com.nids.controller;

import com.nids.model.IntrusionRule;
import com.nids.service.NidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rules")
@RequiredArgsConstructor
public class RuleController {

    private final NidsService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("rules", svc.allRules());
        m.addAttribute("rule", new IntrusionRule());
        return "rules";
    }

    @PostMapping
    public String save(@ModelAttribute IntrusionRule r) {
        svc.saveRule(r);
        return "redirect:/rules";
    }
}
