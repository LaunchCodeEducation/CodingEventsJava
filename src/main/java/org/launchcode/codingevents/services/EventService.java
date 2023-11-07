package org.launchcode.codingevents.services;

import java.util.List;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.dto.EventDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
import org.launchcode.codingevents.exception.UserRegistrationException;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventDetails;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    public List<Event> getAllEventsByCreator(User creator) {
        return eventRepository.findAllByCreator(creator);
    }

    public List<Event> getAllEventsByCurrentUser() {
        return eventRepository.findAllByCreator(userService.getCurrentUser());
    }

    public Event getEventById(int id) {
        return eventRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Event getEventByIdAndCreator(int id, User creator) {
        return eventRepository.findByIdAndCreator(id, creator).orElseThrow(ResourceNotFoundException::new);
    }

    public void removeEventById(int id) {
        eventRepository.deleteById(id);
    }

    public Event save(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());

        EventDetails details = new EventDetails(eventDTO.getDescription(), eventDTO.getContactEmail());
        event.setEventDetails(details);
    
        event.setEventCategory(categoryRepository.findById(eventDTO.getCategoryId()).get());

        event.setCreator(userService.getCurrentUser());

        eventRepository.save(event);

        return event;
    }
}
