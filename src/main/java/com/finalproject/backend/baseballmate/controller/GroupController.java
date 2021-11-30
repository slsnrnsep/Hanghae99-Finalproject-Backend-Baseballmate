package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.responseDto.AllGroupResponseDto;
import com.finalproject.backend.baseballmate.responseDto.GroupDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = {"2. 모임"}) // Swagger
public class GroupController {

    private final GroupService groupService;

    // 모임 생성
    @ApiOperation(value = "(Legacy)모임 게시글 작성", notes = "(Legacy)모임 게시글을 작성합니다.")
    @PostMapping("/groups/legacy")
    public MsgResponseDto createGrouplegacy(@RequestParam(value = "file",required = false) MultipartFile file, GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGrouplegacy(requestDto, userDetails,file);
    }

    // 모임 생성
    @ApiOperation(value = "모임 게시글 작성", notes = "모임 게시글을 작성합니다.")
    @PostMapping("/groups")
    public MsgResponseDto createGroup(@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.createGroup(requestDto, userDetails);
    }

    // 모임페이지 전체 조회

    @ApiOperation(value = "모임 게시글 전체 조회", notes = "모임 게시글을 전체 조회합니다.")
    @GetMapping("/groups")
    public List<AllGroupResponseDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    // 모임페이지 상세 조회
    @ApiOperation(value = "모임 게시글 상세 조회", notes = "모임 게시글중 1개를 상세 조회합니다.")
    @GetMapping("/groups/{groupId}")
    public GroupDetailResponseDto getGroupDetails(@PathVariable("groupId") Long groupId) {
        return groupService.getGroupDetail(groupId);
    }

    // 구단별 모임 조회
    @ApiOperation(value = "구단별로 모임을 조회", notes = "구단정보를 입력하여 구단별로 모임을 조회합니다.")
    @GetMapping(path="/groups",params = "team")
    public List<AllGroupResponseDto> showGroupsByTeam (@RequestParam("team") String selectTeam) throws UnsupportedEncodingException {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt"));
        URLDecoder.decode(selectTeam,"UTF-8");
        return groupService.showGroupsByTeam(selectTeam,pageRequest);
    }

    // 모임 수정하기 - 모임을 생성한 사람만 수정할 수 있게
    @ApiOperation(value = "(Legacy)모임 게시글 수정", notes = "(Legacy)모임 게시글을 수정합니다.")
    @RequestMapping(value = "/groups/{groupId}/legacy", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public MsgResponseDto updateGrouplegacy(@PathVariable Long groupId, @RequestPart(required = false, value = "file") MultipartFile file, GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return groupService.updateGrouplegacy(groupId, file, requestDto, userDetails);
    }

    @ApiOperation(value = "모임 게시글 수정", notes = "모임 게시글을 수정합니다.")
    @PutMapping(value = "/groups/{groupId}")
    public MsgResponseDto updateGroup(@PathVariable Long groupId,@RequestBody GroupRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return groupService.updateGroup(groupId, requestDto, userDetails);
    }

    // 모임 삭제하기 - 모임을 생성한 사람만 삭제할 수 있게
    @ApiOperation(value = "모임 게시글 삭제", notes = "모임 게시글을 삭제합니다.")
    @DeleteMapping("/groups/{groupId}")
    public MsgResponseDto deleteGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.deleteGroup(groupId, userDetails);
    }



    // 모임 확정내기
    @ApiOperation(value = "모임 확정내기(방장만)", notes = "모임을 확정시켜 참가자를 못 받게합니다.")
    @PatchMapping("/groups/{groupId}/applications")
    public MsgResponseDto denyGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return groupService.denyGroup(groupId, userDetails);
    }

    // 모임 참여신청하기
    @ApiOperation(value = "모임에 참여신청하기(방장제외)", notes = "모임에 참여신청을 합니다.")
    @PostMapping("/groups/{groupId}/applications")
    public MsgResponseDto applyGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.applyGroup(groupId, userDetails);
    }

    // 모임 참가신청 취소하기
    @ApiOperation(value = "모임에 한 참여신청 취소하기(방장제외)", notes = "모임에 한 참여신청을 취소 합니다.")
    @DeleteMapping("/groups/{groupId}/applications")
    public MsgResponseDto cancelApply(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupService.cancelApplication(groupId, userDetails);
    }
}
