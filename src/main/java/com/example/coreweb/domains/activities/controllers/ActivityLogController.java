package com.example.coreweb.domains.activities.controllers;

import com.example.coreweb.domains.activities.models.dto.ActivityDto;
import com.example.coreweb.domains.activities.models.entities.Activity;
import com.example.coreweb.domains.activities.services.ActivityService;
import com.example.coreweb.domains.base.models.enums.SortByFields;
import com.example.coreweb.routing.Route;
import com.example.coreweb.utils.PageableParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityLogController {
    private final ActivityService activityService;

    public ActivityLogController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(Route.V1.ADMIN_SEARCH_ACTIVITIES)
    private ResponseEntity<Page<ActivityDto>> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort_by", defaultValue = "ID") SortByFields sortByFields,
            @RequestParam(value = "sort_direction", defaultValue = "DESC") Sort.Direction direction
    ) {
        var activities = this.activityService.search(
                PageableParams.of(query, page, size, sortByFields, direction)
        );
        return ResponseEntity.ok(
                activities.map(a -> new ActivityDto(
                        a.getUserAgent(),
                        a.getIp(),
                        a.getExpires(),
                        a.getRequestMethod(),
                        a.getUrl(),
                        a.getUser().getId(),
                        a.getUser().getUsername(),
                        a.getTotalVisitors()
                ))
        );
    }


}
