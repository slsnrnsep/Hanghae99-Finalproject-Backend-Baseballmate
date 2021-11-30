package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.requestDto.AllCommunityDto;
import com.finalproject.backend.baseballmate.requestDto.CommunityRequestDto;
import com.finalproject.backend.baseballmate.responseDto.CommunityDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"7. 커뮤니티"}) // Swagger
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/community/legacy")
    @ApiOperation(value = "(Legacy)커뮤니티 작성", notes = "(Legacy)커뮤니티 게시글을 작성합니다.")
    public MsgResponseDto postCommunitylegacy(@RequestPart(value = "file",required = false) MultipartFile files, CommunityRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityService.createCommunitylegacy(userDetails, requestDto,files);
    }

    @PostMapping("/community")
    @ApiOperation(value = "커뮤니티 작성", notes = "커뮤니티 게시글을 작성합니다.")
    public MsgResponseDto postCommunity(@RequestBody CommunityRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityService.createCommunity(userDetails, requestDto);
    }

    // 커뮤 전체페이지 조회
    @ApiOperation(value = "커뮤니티 전체 조회", notes = "커뮤니티게시글을 모두 조회합니다.")
    @GetMapping("/community")
    public List<AllCommunityDto> getCommunity() throws ParseException {
        return communityService.getCommunity();
    }

    // 커뮤 상세 조회
    @ApiOperation(value = "커뮤니티 상세 조회", notes = "커뮤니티게시글을 중 1개를 상세조회합니다.")
    @GetMapping("/community/{communityId}")
    public CommunityDetailResponseDto communityDetail(@PathVariable("communityId") Long communityId) {
        return communityService.getCommunityDetail(communityId);
    }

    // 커뮤 수정
    @ApiOperation(value = "커뮤니티 수정", notes = "커뮤니티 게시글을 수정합니다.")
    @PutMapping("/community/{communityId}")
    public MsgResponseDto updateCommunity(@PathVariable("communityId") Long communityId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(required = false, value = "file") MultipartFile file, CommunityRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return communityService.updateCommunity(communityId, userDetails,file, requestDto);
    }

    //커뮤 삭제
    @ApiOperation(value = "커뮤니티 삭제", notes = "커뮤니티 게시글을 삭제합니다.")
    @DeleteMapping("/community/{communityId}")
    public MsgResponseDto deleteCommunity(@PathVariable Long communityId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityService.deleteCommunity(communityId, userDetails);
    }
}
