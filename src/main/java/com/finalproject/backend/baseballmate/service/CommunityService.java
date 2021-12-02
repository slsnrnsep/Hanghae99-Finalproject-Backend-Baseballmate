package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.CommunityCommentRepository;
import com.finalproject.backend.baseballmate.repository.CommunityLikesRepository;
import com.finalproject.backend.baseballmate.repository.CommunityRepository;
import com.finalproject.backend.baseballmate.requestDto.AllCommunityDto;
import com.finalproject.backend.baseballmate.requestDto.CommunityRequestDto;
import com.finalproject.backend.baseballmate.responseDto.CommunityDetailResponseDto;
import com.finalproject.backend.baseballmate.responseDto.MsgResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.utils.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private String commonPath = "/images"; // 파일 저장할 기본 경로 변수 설정, 초기화
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityLikesRepository communityLikesRepository;
    private final FileService fileService;
    String[] picturelist = {"basic0","basic1","basic2","basic3","basic4","basic5","basic6","basic7","basic8","basic9"};
//    String[] picturelist = {"basic0.jpg","basic1.jpg","basic2.jpg","basic3.jpg","basic4.jpg","basic5.jpg","basic6.jpg","basic7.jpg","basic8.jpg","basic9.jpg"};
    Random random = new Random();

    @Transactional
    public MsgResponseDto createCommunitylegacy(UserDetailsImpl userDetails, CommunityRequestDto requestDto,MultipartFile files) {

        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 한 사용자만 등록 가능합니다");
        }
        try {
            String filename = picturelist[random.nextInt(10) + 1];
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
            String communityUserPicture = loginedUser.getPicture();
            String myTeam = loginedUser.getMyselectTeam();
            Long userId = loginedUser.getId();
            String usertype = "";
            if(loginedUser.getKakaoId() == null)
            {
                usertype = "normal";
            }
            else
            {
                usertype = "kakao";
            }
            Community community = new Community(loginedUser, requestDto, communityUserPicture, myTeam,userId,usertype);
            communityRepository.save(community);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "등록완료");
            return msgResponseDto;
        } catch (Exception e) {
            MsgResponseDto msgResponseDto = new MsgResponseDto("fail", "등록실패");
            return msgResponseDto;
        }
    }

    @Transactional
    public MsgResponseDto createCommunity(UserDetailsImpl userDetails, CommunityRequestDto requestDto) {
        if (userDetails == null) {
            throw new IllegalArgumentException("로그인 한 사용자만 등록 가능합니다");
        }

        User loginedUser = userDetails.getUser();
        String communityUserPicture = loginedUser.getPicture();
        String myTeam = loginedUser.getMyselectTeam();
        Long userId = loginedUser.getId();
        String usertype = "";
        if (loginedUser.getKakaoId() == null) {
            usertype = "normal";
        } else {
            usertype = "kakao";
        }
        if (requestDto.getFilePath() == "") {
            requestDto.setFilePath(picturelist[random.nextInt(10) + 1]);
        }
        Community community = new Community(loginedUser, requestDto, communityUserPicture, myTeam, userId, usertype);
        communityRepository.save(community);
        MsgResponseDto msgResponseDto = new MsgResponseDto("success", "등록완료");
        return msgResponseDto;
    }

    @Transactional
    public List<AllCommunityDto> getCommunity() throws ParseException {
        List<Community> communityList = communityRepository.findAllByOrderByCreatedAtDesc();

        List<AllCommunityDto> data = new ArrayList<>();
        for (int i = 0; i<communityList.size(); i++) {
            Community community = communityList.get(i);

            Long communityId = community.getCommunityId();
            String userName = community.getUserName();
            String content = community.getContent();
            String communityUserPicture = community.getCommunityUserPicture();
            String filePath = community.getFilePath();
            String myTeam = community.getMyTeam();
            String dayBefore = getDayBefore(community);
            List<CommunityComment> communityCommentList = communityCommentRepository.findAllByCommunity_CommunityIdOrderByCreatedAtAsc(communityId);
            List<CommunityLikes> communityLikesList = communityLikesRepository.findAllByCommunitylikes_CommunityId(community.getCommunityId());
//            List<CommunityComment> communityCommentList = communityCommentRepository.countAllByCommunityCommentId(communityId);

            Long userId = community.getUserId();
            String usertype = community.getUsertype();

            AllCommunityDto communityDto =
                    new AllCommunityDto(communityId, userName, content, communityUserPicture, filePath, myTeam,dayBefore,communityCommentList,communityLikesList, userId,usertype);
            data.add(communityDto);
        }
        return data;
    }

    public CommunityDetailResponseDto getCommunityDetail(Long communityId) {
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다")
        );

        String userName = community.getUserName();
        String content = community.getContent();
        String communityUserPicture = community.getCommunityUserPicture();
        String filePath = community.getFilePath();
        String myTeam = community.getMyTeam();
        List<CommunityComment> communityCommentList = communityCommentRepository.findAllByCommunity_CommunityIdOrderByCreatedAtAsc(communityId);
        Long userId = community.getUserId();
        String usertype = community.getUsertype();

        CommunityDetailResponseDto communityDetailResponseDto =
                new CommunityDetailResponseDto(userName, content, communityUserPicture, filePath, myTeam, communityCommentList,userId,usertype);
        return communityDetailResponseDto;
    }

    @Transactional
    public MsgResponseDto updateCommunity(Long communityId,  UserDetailsImpl userDetails,CommunityRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 사용자만이 수정할 수 있습니다");
        }

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Community community = communityRepository.findByCommunityId(communityId);

        if (community != null) {
            createdUserId = community.getCreatedUser().getUserid();
            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
            community.updateCommunity(requestDto);
            communityRepository.save(community);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "수정완료");
            return msgResponseDto;
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    public MsgResponseDto deleteCommunity(Long communityId, UserDetailsImpl userDetails) {
        if(userDetails == null)
        {
            throw new IllegalArgumentException("로그인 하신 후 이용해주세요.");
        }
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Community community = communityRepository.findByCommunityId(communityId);
        if (community != null) {
            createdUserId = community.getCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
            communityRepository.deleteById(communityId);
            MsgResponseDto msgResponseDto = new MsgResponseDto("success", "삭제완료");
            return msgResponseDto;
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    public String getDayBefore(Community community) throws ParseException {

        //LocalDateTime -> Date으로 변환
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = community.getCreatedAt();

        Date format1 = java.sql.Timestamp.valueOf(now);
        Date format2 = java.sql.Timestamp.valueOf(createdAt);


        // date.getTime() : Date를 밀리세컨드로 변환. 1초 = 1000밀리초
        Long diffSec = (format1.getTime() - format2.getTime()) / 1000; // 초 차이
        Long diffMin = (format1.getTime() - format2.getTime()) / 60000; // 분 차이
        Long diffHour = (format1.getTime() - format2.getTime()) / 3600000; // 시 차이, 24시까지 나옴
        Long diffDays = diffSec / (24 * 60 * 60); // 일자수 차이 예:7일, 6일

        //DayBefore 계산
        // 초 차이가 60초 미만일 때 -> return 초 차이
        // 초 차이가 60초 이상, 분 차이가 60분 미만일 때 -> return 분 차이
        // 분 차이가 60분 이상, 시 차이가 24 미만일 때 -> return 시 차이
        // 시 차이가 24 이상, 일 차이가 7일 미만일 때 -> return 일자수 차이
        // 일 차이가 7일 이상일 때 -> return createdAt의 년, 월, 일까지

        String dayBefore = "";

        if(diffSec < 60) {
            String secstr = diffSec.toString();
            dayBefore = secstr + "초 전";
        } else if(diffSec >= 60 && diffMin < 60) {
            String minstr = diffMin.toString();
            dayBefore = minstr + "분 전";
        } else if(diffMin >= 60 && diffHour < 24) {
            String hourstr = diffHour.toString();
            dayBefore = hourstr + "시간 전";
        } else if(diffHour >= 24 && diffDays < 7) {
            String daystr = diffDays.toString();
            dayBefore = daystr + "일 전";
        } else if (diffDays > 7) {
            dayBefore = format2.toString();
        }
        return dayBefore;
    }
}
