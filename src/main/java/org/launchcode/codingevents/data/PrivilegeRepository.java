package org.launchcode.codingevents.data;

import org.launchcode.codingevents.models.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Integer> {

    Privilege findByName(String name);

}

