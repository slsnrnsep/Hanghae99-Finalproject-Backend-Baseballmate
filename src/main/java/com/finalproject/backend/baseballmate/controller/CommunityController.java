package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.requestDto.AllCommunityDto;
import com.finalproject.backend.baseballmate.requestDto.CommunityRequestDto;
import com.finalproject.backend.baseballmate.responseDto.CommunityDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.CommunityService;
import com.finalproject.backend.baseballmate.service.FileService;
import com.finalproject.backend.baseballmate.utils.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private String commonPath = "/images";
    private final FileService fileService;
    String[] picturelist = {"basic0.jpg","basic1.jpg","basic2.jpg","basic3.jpg","basic4.jpg","basic5.jpg","basic6.jpg","basic7.jpg","basic8.jpg","basic9.jpg"};
    Random random = new Random();


    @PostMapping("/community")
    public MsgResponseDto postCommunity(
            @RequestPart(value = "file",required = false) MultipartFile files,
            CommunityRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 등록 가능합니다");
        }
        try
        {
            String filename = picturelist[random.nextInt(10)+1];
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
            communityService.createCommunity(loginedUser, requestDto);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "등록완료");
            return msgResponseDto;

        } catch (Exception e) {
            MsgResponseDto msgResponseDto = new MsgResponseDto("fail", "등록실패");
            return msgResponseDto;
        }

    }

    // 커뮤 전체페이지 조회
    @GetMapping("/community")
    public List<AllCommunityDto> getCommunity() throws ParseException
    {
        List<AllCommunityDto> allCommunity = communityService.getCommunity();
        return allCommunity;
    }

    // 커뮤 상세 조회
    @GetMapping("/community/{communityId}")
    public CommunityDetailResponseDto communityDetail(@PathVariable("communityId") Long communityId)
    {
        CommunityDetailResponseDto communityDetailResponseDto = communityService.getCommunityDetail(communityId);
        return communityDetailResponseDto;
    }
    // 커뮤 수정
    @PutMapping("/community/{communityId}")
    public MsgResponseDto updateCommunity(
            @PathVariable("communityId") Long communityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(required = false, value = "file") MultipartFile file,
            CommunityRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 사용자만이 수정할 수 있습니다");
        }
        try {
            communityService.updateCommunity(communityId, userDetails,file, requestDto);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정완료");
            return msgResponseDto;
        } catch (Exception e) {
            MsgResponseDto msgResponseDto = new MsgResponseDto("fail", "수정실패");
            return msgResponseDto;
        }
    }


    @DeleteMapping("/community/{communityId}")
    public MsgResponseDto deleteCommunity(
            @PathVariable Long communityId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        communityService.deleteCommunity(communityId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제완료");
        return msgResponseDto;
    }
}
