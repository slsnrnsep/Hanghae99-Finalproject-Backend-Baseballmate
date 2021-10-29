package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.GroupRepository;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;

    // 모임페이지 전체 조회 :
    @GetMapping("/page/group")
    public List<AllGroupResponseDto> getAllGroups() {
        List<AllGroupResponseDto> groupList = groupService.getAllGroups();
        return groupList;
    }

    // 모임 생성
    @PostMapping("/page/group")
    public MsgResponseDto createGroup(@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인한 유저의 유저네임 가져오기
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 이용자만 모임을 생성할 수 있습니다.");
        }
        User loginedUser = userDetails.getUser();
        String loginedUsername = userDetails.getUser().getUsername();
        groupService.createGroup(requestDto, loginedUser);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 등록 성공");

        return msgResponseDto;
    }

    // 모임페이지 상세 조회
    @GetMapping("/page/group/detail/{groupId}")
    public GroupDetailResponseDto getGroupDetails(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 모임을 조회할 수 있습니다.");
        }
//        String loginedUserid = userDetails.getUser().getUserid();
        GroupDetailResponseDto detailResponseDto = groupService.getGroupDetail(groupId);
        return detailResponseDto;
    }

    // 모임 참여신청하기
    @PostMapping("/page/group/detail/apply/{groupId}")
    public MsgResponseDto applyGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        }
        User appliedUser = userDetails.getUser();
        Group appliedGroup = groupRepository.findByGroupId(groupId);
        groupService.applyGroup(appliedUser, appliedGroup);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 완료");
        return msgResponseDto;
    }

    // 모임 수정하기 - 모임을 생성한 사람만 수정할 수 있게
    @PutMapping("/page/group/detail/{groupId}")
    public MsgResponseDto updateGroup(@PathVariable Long groupId, @RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        groupService.updateGroup(groupId, requestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }

    // 모임 삭제하기 - 모임을 생성한 사람만 삭제할 수 있게
    @DeleteMapping("/page/group/detail/{groupId}")
    public MsgResponseDto deleteGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        groupService.deleteGroup(groupId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
        return msgResponseDto;
    }
}
