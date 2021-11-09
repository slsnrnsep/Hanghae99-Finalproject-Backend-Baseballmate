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
import com.finalproject.backend.baseballmate.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private String commonPath = "/images";

    // 모임페이지 전체 조회 :
    @GetMapping("/groups")
    public List<AllGroupResponseDto> getAllGroups()
    {
        List<AllGroupResponseDto> groupList = groupService.getAllGroups();
        return groupList;
    }

    // 구단별 모임 조회
    @GetMapping(path="/groups",params = "team")
    public List<AllGroupResponseDto> showGroupsByTeam (@RequestParam("team") String selectTeam)
    {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt"));
        List<AllGroupResponseDto> groupResponseDtos = groupService.showGroupsByTeam(selectTeam,pageRequest);
        return groupResponseDtos;


    }

    // 모임 생성
    @PostMapping("/groups")
    public MsgResponseDto createGroup(
            @RequestParam(value = "file",required = false) MultipartFile files,
            @RequestBody GroupRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//         로그인한 유저의 유저네임 가져오기
        if (userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 이용자만 모임을 생성할 수 있습니다.");
        }
        try
        {
            String filename = "basic.jpg";
            if (files != null) {
                String origFilename = files.getOriginalFilename();
                filename = new MD5Generator(origFilename).toString() + ".jpg";
                /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */

                String savePath = System.getProperty("user.dir") + commonPath;
                /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                //files.part.getcontententtype() 해서 이미지가 아니면 false처리해야함.
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "/" + filename;// 이경로는 우분투랑 윈도우랑 다르니까 주의해야댐 우분투 : / 윈도우 \\ 인것같음.
                files.transferTo(new File(filePath));
            }

            requestDto.setFilePath(filename);
            User loginedUser = userDetails.getUser();
            String loginedUsername = userDetails.getUser().getUsername();
            groupService.createGroup(requestDto, loginedUser);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 등록 성공");
            return msgResponseDto;
        }

        catch (Exception e)
        {
            MsgResponseDto msgResponseDto = new MsgResponseDto("failed", "모임 등록 실패");
            return msgResponseDto;
        }

    }

    // 모임페이지 상세 조회
    @GetMapping("/groups/{groupId}")
    public GroupDetailResponseDto getGroupDetails(@PathVariable("groupId") Long groupId)
    {
        GroupDetailResponseDto detailResponseDto = groupService.getGroupDetail(groupId);
        return detailResponseDto;
    }

    // 모임 참여신청하기
    @PostMapping("/groups/{groupId}/applications")
    public MsgResponseDto applyGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if (userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        }

        User appliedUser = userDetails.getUser();
        Group appliedGroup = groupRepository.findByGroupId(groupId);
        groupService.applyGroup(appliedUser, appliedGroup);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 완료");
        return msgResponseDto;

    }

    // 모임 참가신청 취소하기
    @DeleteMapping("/groups/{groupId}/applications")
    public MsgResponseDto cancelApply(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        }
        groupService.cancelApplication(groupId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 취소 완료");
        return msgResponseDto;
    }

    // 모임 수정하기 - 모임을 생성한 사람만 수정할 수 있게
    @PutMapping("/groups/{groupId}")
    public MsgResponseDto updateGroup(
            @PathVariable Long groupId,
            @RequestBody GroupRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }

        groupService.updateGroup(groupId, requestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }

    // 모임 삭제하기 - 모임을 생성한 사람만 삭제할 수 있게
    @DeleteMapping("/groups/{groupId}")
    public MsgResponseDto deleteGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        groupService.deleteGroup(groupId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 성공");
        return msgResponseDto;
    }
}
