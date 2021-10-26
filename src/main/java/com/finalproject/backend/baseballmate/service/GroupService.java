package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class GroupService {

    @Transactional
    public Group createGroup(GroupRequestDto requestDto, String username) {
        Group Group = new Group(requestDto, username);
        GroupRepository.save(Group);
        return Group;
    }
}
