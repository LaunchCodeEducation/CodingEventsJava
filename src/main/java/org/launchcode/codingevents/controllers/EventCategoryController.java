package org.launchcode.codingevents.controllers;

import jakarta.validation.Valid;
import org.launchcode.codingevents.dto.EventCategoryDTO;
import org.launchcode.codingevents.services.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping("eventCategories")
public class EventCategoryController {

    @Autowired
    private EventCategoryService eventCategoryService;

    @GetMapping
    public String displayAllCategories(Model model) {
        model.addAttribute("title", "All Categories");
        model.addAttribute("categories", eventCategoryService.getAllCategoriesByCurrentUser());
        return "eventCategories/index";
    }

    @GetMapping("create")
    public String renderCreateEventCategoryForm(Model model) {
        model.addAttribute("title", "Create Category");
        model.addAttribute(new EventCategoryDTO());
        return "eventCategories/create";
    }

    @PostMapping("create")
    public String processCreateEventCategoryForm(@Valid @ModelAttribute EventCategoryDTO eventCategoryDTO,
                                                 Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Category");
            return "eventCategories/create";
        }

        eventCategoryService.save(eventCategoryDTO);
        return "redirect:/eventCategories";
    }
}