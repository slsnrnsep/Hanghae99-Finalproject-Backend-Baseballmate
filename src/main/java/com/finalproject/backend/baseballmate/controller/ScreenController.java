package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.model.Screen;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.ScreenRepository;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.AllScreenResponseDto;
import com.finalproject.backend.baseballmate.requestDto.GroupRequestDto;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ScreenDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.ScreenResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.FileService;
import com.finalproject.backend.baseballmate.service.ScreenService;
import com.finalproject.backend.baseballmate.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenRepository screenRepository;
    private final ScreenService screenService;
    private final String commonPath = "/images";
    private final FileService fileService;

    @PostMapping("/screen/{screenId}/applications")
    public MsgResponseDto applyScreen(
            @PathVariable Long screenId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if (userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        }

        User appliedUser = userDetails.getUser();
        Screen appliedScreen = screenRepository.findByScreenId(screenId);
        screenService.applyScreen(appliedUser, appliedScreen);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 완료");
        return msgResponseDto;
    }

    @DeleteMapping("/screen/{screenId}/applications")
    public MsgResponseDto cancleApply(
            @PathVariable Long screenId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 신청할 수 있습니다.");
        }
        screenService.cancleApplication(screenId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "모임 신청 취소 완료");
        return msgResponseDto;

    }

    @PostMapping("/screen")
    public ScreenResponseDto postScreen(
            @RequestParam(value = "file",required = false) MultipartFile files,
            ScreenRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 이용자만 스크인 야구 모임생성이 가능합니다");
        }
        try {
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
            screenService.createScreen(requestDto, loginedUser);
            ScreenResponseDto screenResponseDto = new ScreenResponseDto("success","등록 성공");
            return screenResponseDto;
        } catch (Exception e) {
            ScreenResponseDto screenResponseDto = new ScreenResponseDto("success","등록 실패");
            return screenResponseDto;
        }
    }
    // 스크린야구 모임 전체 조회
    @GetMapping("/screen")
    public List<AllScreenResponseDto> getAllScreens(){
        List<AllScreenResponseDto> screenList = screenService.getAllScreens();
        return screenList;
    }

    @GetMapping(path="/screen",params = "region")
    public List<AllScreenResponseDto> showScreenByregion (@RequestParam("region") String location)
    {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt"));
        List<AllScreenResponseDto> screenResponseDtos = screenService.showScreenByregion(location,pageRequest);
        return screenResponseDtos;
    }

    // 스크린야구 상세 조회
    @GetMapping("/screen/{screenId}")
    public ScreenDetailResponseDto getScreenDetails(@PathVariable("screenId") Long screenId)
    {
        ScreenDetailResponseDto detailResponseDto = screenService.getScreenDetails(screenId);
        return detailResponseDto;
    }

    // 모임 수정하기 - 모임을 생성한 사람만 수정할 수 있게
    @RequestMapping(value = "/screen/{screenId}", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public MsgResponseDto updateGroup(
            @PathVariable Long screenId,
            @RequestPart(required = false, value = "file") MultipartFile file,
            ScreenRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        screenService.updateScreen(screenId, file, requestDto, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정 완료");
        return msgResponseDto;
    }

    @DeleteMapping("/screen/{screenId}")
    public MsgResponseDto deleteScreen(
            @PathVariable Long screenId,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인을 해주세요");
        }
        screenService.deleteScreen(screenId, userDetails);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제 완료");
        return msgResponseDto;
    }
}
