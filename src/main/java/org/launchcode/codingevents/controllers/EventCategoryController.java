package org.launchcode.codingevents.controllers;

import jakarta.validation.Valid;
import org.launchcode.codingevents.data.EventCategoryRespository;

import org.launchcode.codingevents.models.EventCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("eventCategories")
public class EventCategoryController {

    @Autowired
    private EventCategoryRespository eventCategoryRepository;

    @GetMapping
    public String displayAllCategories(Model model){
        model.addAttribute("title", "All Categories");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "eventCategories/index";
    }

    @GetMapping
    public String renderCreateEventCategoryForm(Model model){
    model.addAttribute("title", "Event Category");
    model.addAttribute(new EventCategory());
      return "eventCategories/create";
    }

    @PostMapping
    public String processCreateEventCategoryForm(@Valid @ModelAttribute EventCategory eventCategory, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Category");
            model.addAttribute(new EventCategory());
            return "eventCategories/create";
        }
        eventCategoryRepository.save(eventCategory);
        return "redirect:";
    }
}
