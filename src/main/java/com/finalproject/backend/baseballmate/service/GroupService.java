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
//import com.finalproject.backend.baseballmate.responseDto.HotGroupReponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
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
        int nowAppliedNum = getNowAppliedNum(groupId);
        int canApplyNum = (peopleLimit - nowAppliedNum);
        String stadium = group.getStadium();
        String groupDate = group.getGroupDate();
        List<GroupComment> groupcommentList = group.getGroupCommentList();

        GroupDetailResponseDto detailResponseDto =
                new GroupDetailResponseDto(createdUserName, title, content, peopleLimit, nowAppliedNum, canApplyNum, stadium , groupDate, groupcommentList);

        return detailResponseDto;
    }

    // 모임 ID별로 현재 참여신청 인원 구하기
    public int getNowAppliedNum(Long groupId) {
        List<GroupApplication> groupApplications = groupApplicationRepository.findAll();

        List<Long> appliedGroupIdList = new ArrayList<>();
        for (int i=0; i<groupApplications.size(); i++) {
            GroupApplication groupApplication = groupApplications.get(i);
            Long appliedGroupId = groupApplication.getAppliedGroup().getGroupId();
            appliedGroupIdList.add(appliedGroupId);
        }

        int nowAppliedNum = 0;
        for (int l=0; l<appliedGroupIdList.size(); l++) {
            Long appliedGroupId = appliedGroupIdList.get(l);
            if (groupId == appliedGroupId) {
                nowAppliedNum = nowAppliedNum + 1;
            }
        }
        return nowAppliedNum;
    }

    // 핫한 모임 구하기
    public void getHotGroup() {
        // (현재 신청 인원수/모임 최대 인원수) * 100 퍼센트 구하기
        //
    }

    // 모임 게시글 수정하기
    public void updateGroup(Long groupId, GroupRequestDto requestDto, UserDetailsImpl userDetails) {
        String loginedUsername = userDetails.getUsername();
        String createdUsername = "";

        Group group = groupRepository.findByGroupId(groupId);
        if(group != null) {
            createdUsername = group.getCreatedUsername();

            if(!loginedUsername.equals(createdUsername)) {
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
            group.updateGroup(requestDto);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    //모임 게시글 삭제하기
    public void deleteGroup(Long groupId, UserDetailsImpl userDetails) {
        String loginedUsername = userDetails.getUsername();
        String createdUsername = "";

        Group group = groupRepository.findByGroupId(groupId);
        if(group != null) {
            createdUsername = group.getCreatedUsername();

            if(!loginedUsername.equals(createdUsername)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            groupRepository.deleteById(groupId);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // 모임 참여하기
    @Transactional
    public void applyGroup(User appliedUser, Group appliedGroup) {
        GroupApplication groupApplication = new GroupApplication(appliedUser, appliedGroup);
        groupApplicationRepository.save(groupApplication);
    }


}
