package org.launchcode.codingevents.controllers;

import jakarta.validation.Valid;
import org.launchcode.codingevents.dto.EventDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.services.EventCategoryService;
import org.launchcode.codingevents.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

/**
 * Created by Chris Bay
 */
@Controller
@RequestMapping("events")
@PreAuthorize("hasRole('ROLE_USER')")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventCategoryService eventCategoryService;

    @GetMapping
    public String displayEvents(@RequestParam(required = false) Integer categoryId, Model model) {
        if (categoryId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventService.getAllEvents());
        } else {
            try {
                EventCategory category = eventCategoryService.getCategoryById(categoryId);

                model.addAttribute("title", "Events in category: " + category.getName());
                model.addAttribute("events", category.getEvents());
            } catch(ResourceNotFoundException ex) {
                model.addAttribute("title", "Invalid Category ID: " + categoryId);
            }
        }

        return "events/index";
    }

    @GetMapping("attending")
    public String displayMyEvents(Model model) {
        model.addAttribute("events", eventService.getAttendingEventsByCurrentUser());
        model.addAttribute("title", "My Events");
        return "events/index";
    }

    @PostMapping("{id}/attending")
    public String processUserEventAttendance(@PathVariable Integer id, Model model) {

        eventService.addAttendanceForCurrentUser(id);

        return "redirect:/events/detail?eventId=" + id;
    }

    @PostMapping("{id}/removeAttending")
    public String removeUserEventAttendance(@PathVariable Integer id, Model model) {

        eventService.removeAttendanceForCurrentUser(id);

        return "redirect:/events/detail?eventId=" + id;
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @GetMapping("organizer")
    public String displayOrganizerEvents(Model model) {

        model.addAttribute("title", "My Organizer Events");
        model.addAttribute("events", eventService.getAllEventsByCurrentUser());

        return "events/index";
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new EventDTO());
        model.addAttribute("categories", eventCategoryService.getAllCategories());
        return "events/create";
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @PostMapping("create")
    public String processCreateEventForm(@ModelAttribute @Valid EventDTO newEventDTO,
                                         Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            model.addAttribute("categories", eventCategoryService.getAllCategories());
            return "events/create";
        }

        eventService.save(newEventDTO);
        return "redirect:/events";
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events", eventService.getAllEventsByCurrentUser());
        return "events/delete";
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {

        if (eventIds != null) {
            for (int id : eventIds) {
                eventService.removeEventById(id);
            }
        }

        return "redirect:/events";
    }

    @GetMapping("detail")
    public String displayEventDetails(@RequestParam Integer eventId, Model model) {
        try {
            Event event = eventService.getEventByIdForCurrentUser(eventId);

            model.addAttribute("title", event.getName() + " Details");
            model.addAttribute("event", event);
            model.addAttribute("userAttendance", eventService.getUserEventAttendance(event));
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("title", "Invalid Event ID: " + eventId);
        }

        return "events/detail";
    }

}
