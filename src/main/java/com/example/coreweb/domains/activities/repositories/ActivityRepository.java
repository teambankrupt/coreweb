package com.example.coreweb.domains.activities.repositories;

import com.example.coreweb.domains.activities.models.entities.Activity;
import com.example.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a WHERE (:query IS NULL OR (a.user.name LIKE %:query% OR a.user.username LIKE %:query%)) AND a.deleted=FALSE")
    Page<Activity> search(
            @Param("query") String query,
            Pageable pageable
    );

    Activity findFirstBy();

    Activity findFirstByUserOrderByIdDesc(User user);

    Page<Activity> findByUser(User user, Pageable pageable);
}
