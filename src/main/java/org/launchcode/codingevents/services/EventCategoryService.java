package org.launchcode.codingevents.services;

import java.util.List;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.dto.EventCategoryDTO;
import org.launchcode.codingevents.exception.ResourceNotFoundException;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventCategoryService {

    @Autowired
    private EventCategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public List<EventCategory> getAllCategories() {
        return (List<EventCategory>) categoryRepository.findAll();
    }

    public List<EventCategory> getAllCategoriesByCreator(User creator) {
        return categoryRepository.findAllByCreator(creator);
    }

    public List<EventCategory> getAllCategoriesByCurrentUser() {
        return categoryRepository.findAllByCreator(userService.getCurrentUser());
    }

    public EventCategory getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public EventCategory getCategoryByIdAndCreator(int id, User creator) {
        return categoryRepository.findByIdAndCreator(id, creator).orElseThrow(ResourceNotFoundException::new);
    }

    public EventCategory getCategoryByIdForCurrentUser(int id) {
        return getCategoryByIdAndCreator(id, userService.getCurrentUser());
    }

    public EventCategory save(EventCategoryDTO categoryDTO) {
        EventCategory category = new EventCategory();
        category.setName(categoryDTO.getName());
        category.setCreator(userService.getCurrentUser());

        categoryRepository.save(category);

        return category;
    }

}

