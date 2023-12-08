package org.launchcode.codingevents.services;

import java.util.List;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.dto.EventDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
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
        return getAllEventsByCreator(userService.getCurrentUser());
    }

    public Event getEventById(int id) {
        return eventRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Event getEventByIdAndCreator(int id, User creator) {
        return eventRepository.findByIdAndCreator(id, creator).orElseThrow(ResourceNotFoundException::new);
    }

	public Event getEventByIdForCurrentUser(int id) {
        return getEventByIdAndCreator(id, userService.getCurrentUser());
	}

    public void removeEventById(int id) {
        eventRepository.deleteById(id);
    }

    public Event save(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());

        EventDetails details = new EventDetails(eventDTO.getDescription(),
                                                eventDTO.getContactEmail());
        event.setEventDetails(details);
    
        event.setEventCategory(categoryRepository.findById(eventDTO.getCategoryId())
                                                 .orElse(null));

        event.setCreator(userService.getCurrentUser());

        eventRepository.save(event);

        return event;
    }

    public void addAttendanceForUser(Integer eventId, User user) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(ResourceNotFoundException::new);

        if (!event.getAttendees().contains(user)) {
            event.getAttendees().add(user);
            eventRepository.save(event);
        }
    }

    public void removeAttendanceForUser(Integer eventId, User user) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(ResourceNotFoundException::new);

        event.getAttendees().remove(user);
        eventRepository.save(event);
    }

    public void removeAttendanceForCurrentUser(Integer eventId) {
        removeAttendanceForUser(eventId, userService.getCurrentUser());
    }

    public void addAttendanceForCurrentUser(Integer eventId) {
        addAttendanceForUser(eventId, userService.getCurrentUser());
    }

    public boolean getUserEventAttendance(Event event) {
        return event.getAttendees().contains(userService.getCurrentUser());
    }

    public List<Event> getAttendingEventsByCurrentUser() {
        return (List<Event>) userService.getCurrentUser().getAttendingEvents();
    }
}
