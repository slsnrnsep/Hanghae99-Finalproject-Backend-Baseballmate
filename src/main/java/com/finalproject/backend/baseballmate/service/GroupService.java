package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    // 모임 전체 조회
    public AllGroupResponseDto getAllGroups() {
        List<Group> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
        AllGroupResponseDto allGroupResponseDto = new AllGroupResponseDto("success", groupList);
        System.out.println(allGroupResponseDto);
        return allGroupResponseDto;
    }

    // 모임 형성
    @Transactional
    public Group createGroup(GroupRequestDto requestDto, String madeUser) {
        Group Group = new Group(requestDto, madeUser);
        groupRepository.save(Group);
        return Group;
    }

    // 모임 상세 조회
//    public GroupDetailResponseDto getGroupDetail(Long groupId) {
//
//    }
}
