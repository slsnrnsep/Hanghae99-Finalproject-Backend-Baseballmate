package com.finalproject.backend.baseballmate.controller;

import com.finalproject.backend.baseballmate.responseDto.*;
import com.finalproject.backend.baseballmate.requestDto.ScreenRequestDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.service.ScreenService;
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
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"3. 스크린모임"}) // Swagger

public class ScreenController {

    private final ScreenService screenService;

    // 스크린모임 생성
    @ApiOperation(value = "(Legacy)스크린모임 게시글 작성", notes = "(Legacy)스크린모임 게시글을 작성합니다.")
    @PostMapping("/screen/legacy")
    public ScreenResponseDto postScreenlegacy(@RequestParam(value = "file",required = false) MultipartFile files, ScreenRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return screenService.createScreenlegacy(requestDto, userDetails,files);
    }

    // 스크린모임 생성
    @ApiOperation(value = "스크린모임 게시글 작성", notes = "스크린모임 게시글을 작성합니다.")
    @PostMapping("/screen")
    public ScreenResponseDto postScreen(@RequestBody ScreenRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return screenService.createScreen(requestDto, userDetails);
    }
    // 스크린야구 모임 전체 조회
    @GetMapping("/screen")
    @ApiOperation(value = "스크린모임 게시글 전체 조회", notes = "스크린모임 게시글을 전체 조회합니다.")
    public List<AllScreenResponseDto> getAllScreens(){
        return screenService.getAllScreens();
    }

    // 스크린야구 상세 조회
    @GetMapping("/screen/{screenId}")
    @ApiOperation(value = "스크린모임 게시글 상세 조회", notes = "스크린모임 게시글중 1개를 상세 조회합니다.")
    public ScreenDetailResponseDto getScreenDetails(@PathVariable("screenId") Long screenId)
    {
        return screenService.getScreenDetails(screenId);
    }

    // 스야 조회(최신 등록 순)
    @ApiOperation(value = "최신순으로 스크린모임을 조회", notes = "작성한 최신순으로 스크린모임을 조회합니다.")
    @GetMapping(path = "/screen", params = "count")
    public List<AllScreenResponseDto> getNowScreen(@RequestParam("count") int number) throws ParseException
    {
        return screenService.getnowScreen(number);
    }

    // 스야 조회(인기 순)
    @ApiOperation(value = "인기순으로 스크린모임을 조회", notes = "가장 마감임박한 순서로 스크린모임을 조회합니다.")
    @GetMapping("/groups/hotscreen")
    public List<HotScreenResponseDto> getHotScreenList(){
        return screenService.getHotScreen();
    }

    // 지역별 스크린모임 조회
    @ApiOperation(value = "지역별로 스크린모임을 조회", notes = "지역정보를 입력하여 지역별로 스크린모임을 조회합니다.")
    @GetMapping(path="/screen",params = "region")
    public List<AllScreenResponseDto> showScreenByRegion (@RequestParam("region") String location) throws UnsupportedEncodingException {
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdAt"));
        URLDecoder.decode(location,"UTF-8");
        return screenService.showScreenByregion(location,pageRequest);
    }

    // 스크린모임 수정하기 - 모임을 생성한 사람만 수정할 수 있게
    @ApiOperation(value = "스크린모임 게시글 수정", notes = "스크린모임 게시글을 수정합니다.")
    @RequestMapping(value = "/screen/{screenId}", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public MsgResponseDto updateGroup(@PathVariable Long screenId, @RequestPart(required = false, value = "file") MultipartFile file, ScreenRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return screenService.updateScreen(screenId, file, requestDto, userDetails);
    }

    // 스크린모임 삭제하기 - 모임을 생성한 사람만 삭제할 수 있게
    @ApiOperation(value = "스크린모임 게시글 삭제", notes = "스크린모임 게시글을 삭제합니다.")
    @DeleteMapping("/screen/{screenId}")
    public MsgResponseDto deleteScreen(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenService.deleteScreen(screenId, userDetails);
    }

    //스야모임 확정내기
    @ApiOperation(value = "스크린모임 확정내기(방장만)", notes = "스크린모임을 확정시켜 참가자를 못 받게합니다.")
    @PatchMapping("/screen/{screenId}/applications")
    public MsgResponseDto denyGroup(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return screenService.denyScreen(screenId, userDetails);
    }
    // 스크린모임 참여신청하기
    @ApiOperation(value = "스크린모임에 참여신청하기(방장제외)", notes = "스크린모임에 참여신청을 합니다.")
    @PostMapping("/screen/{screenId}/applications")
    public MsgResponseDto applyScreen(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenService.applyScreen(screenId, userDetails);
    }

    // 스크린모임 참가신청 취소하기
    @ApiOperation(value = "스크린모임에 한 참여신청 취소하기(방장제외)", notes = "스크린모임에 한 참여신청을 취소 합니다.")
    @DeleteMapping("/screen/{screenId}/applications")
    public MsgResponseDto cancelApply(@PathVariable Long screenId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return screenService.cancleApplication(screenId, userDetails);
    }



}
