package com.nids.controller;

import com.nids.model.AlertReview;
import com.nids.service.NidsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final NidsService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("alerts",  svc.allAlerts());
        m.addAttribute("reviews", svc.allReviews());
        m.addAttribute("review",  new AlertReview());
        return "alerts";
    }

    @PostMapping("/review")
    public String review(@ModelAttribute AlertReview review,
                         @RequestParam Integer alertId) {
        review.setAlert(svc.getAlert(alertId));
        svc.saveReview(review);
        return "redirect:/alerts";
    }
}
