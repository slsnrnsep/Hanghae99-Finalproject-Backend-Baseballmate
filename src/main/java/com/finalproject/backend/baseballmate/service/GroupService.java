package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupApplication;
import com.finalproject.backend.baseballmate.model.GroupComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupApplicationRepository;
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
    private final GroupApplicationRepository groupApplicationRepository;

    // 모임 전체 조회
    public AllGroupResponseDto getAllGroups() {
        List<Group> groupList = groupRepository.findAllByOrderByCreatedAtDesc();
        AllGroupResponseDto allGroupResponseDto = new AllGroupResponseDto("success", groupList);
        System.out.println(allGroupResponseDto);
        return allGroupResponseDto;
    }

    // 모임 형성
    @Transactional
    public Group createGroup(GroupRequestDto requestDto, String loginedUsername) {
        Group Group = new Group(requestDto, loginedUsername);
        groupRepository.save(Group);
        return Group;
    }

    // 모임 상세 조회
    public GroupDetailResponseDto getGroupDetail(Long groupId) {
        // 모임 entity에서 해당 모임에 대한 모든 정보 빼오기
        Group group = groupRepository.findByGroupId(groupId);

        String createdUserName = group.getCreatedUsername();
        String title = group.getTitle();
        String content = group.getContent();
        int peopleLimit = group.getPeopleLimit();
        String stadium = group.getStadium();
        String groupDate = group.getGroupDate();
        List<GroupComment> groupcommentList = group.getGroupCommentList();

        GroupDetailResponseDto detailResponseDto =
                new GroupDetailResponseDto(createdUserName, title, content, peopleLimit, stadium, groupDate, groupcommentList);

        return detailResponseDto;
    }

    // 모임 참여하기
    @Transactional
    public void applyGroup(User appliedUser, Group appliedGroup) {
        GroupApplication groupApplication = new GroupApplication(appliedUser, appliedGroup);
        groupApplicationRepository.save(groupApplication);
    }
}
