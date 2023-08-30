package org.launchcode.codingevents.data;

import java.util.List;
import java.util.Optional;

import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris Bay
 */
@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {

    List<Event> findAllByCreator(User creator);
    Optional<Event> findByIdAndCreator(Integer id, User creator);
}