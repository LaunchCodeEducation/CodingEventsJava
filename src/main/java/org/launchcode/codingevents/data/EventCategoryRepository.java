package org.launchcode.codingevents.data;

import java.util.List;
import java.util.Optional;

import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris Bay
 */
@Repository
public interface EventCategoryRepository extends CrudRepository<EventCategory, Integer> {

    List<EventCategory> findAllByCreator(User creator);
    Optional<EventCategory> findByIdAndCreator(Integer id, User creator);
}