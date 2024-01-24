package org.launchcode.codingevents.data;

import java.util.List;

import org.launchcode.codingevents.models.Tag;
import org.launchcode.codingevents.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chris Bay
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    List<Tag> findAllByCreator(User creator);
}
