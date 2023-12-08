package com.example.coreweb.domains.activities.services;

import com.example.coreweb.domains.activities.models.entities.Activity;
import com.example.auth.entities.User;
import com.example.coreweb.utils.PageableParams;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityService {
    Page<Activity> search(String username,PageableParams params);

    Activity save(Activity activity);

    Activity findFirst();

    Activity findLast(User user);

    Page<Activity> findByUser(User user, int page, int size);

    Activity findOne(long id);

    List<Activity> findAll();

    void delete(Long id);
}
