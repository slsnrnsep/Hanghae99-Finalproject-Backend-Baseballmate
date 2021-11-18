package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.Group;

import java.util.List;

public interface GroupRepositoryCustom {
    List<Group> searchTeamHotgroup(String myteam);
}
