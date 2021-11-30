package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.*;
import com.finalproject.backend.baseballmate.repository.CommunityCommentRepository;
import com.finalproject.backend.baseballmate.repository.CommunityLikesRepository;
import com.finalproject.backend.baseballmate.repository.CommunityRepository;
import com.finalproject.backend.baseballmate.requestDto.AllCommunityDto;
import com.finalproject.backend.baseballmate.requestDto.CommunityRequestDto;
import com.finalproject.backend.baseballmate.responseDto.CommunityDetailResponseDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import com.finalproject.backend.baseballmate.util.MD5Generator;
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

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private String commonPath = "/images"; // 파일 저장할 기본 경로 변수 설정, 초기화
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityLikesRepository communityLikesRepository;

    @Transactional
    public Community createCommunity(User loginedUser, CommunityRequestDto requestDto) {
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
        return community;
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
                new CommunityDetailResponseDto(userName, content, communityUserPicture, filePath, myTeam, communityCommentList, userId,usertype);
        return communityDetailResponseDto;
    }

    @Transactional
    public void updateCommunity(Long communityId,  UserDetailsImpl userDetails, MultipartFile file,CommunityRequestDto requestDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Community community = communityRepository.findByCommunityId(communityId);

        if (community != null) {
            createdUserId = community.getCreatedUser().getUserid();
            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
            if (file != null) {
                String origFilename = file.getOriginalFilename();
                String filename = new MD5Generator(origFilename).toString() + "jpg";

                String savePath = System.getProperty("user.dir") + commonPath;

                // 파일이 저장되는 폴더가 없을 경우 폴더 생성
                if (!new java.io.File(savePath).exists()) {
                    try {
                        new java.io.File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

                // 이미지 파일 저장
                String filePath = savePath + "/" + filename;
                try{
                    file.transferTo(new File(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                community.setFilePath(filename);
            }
            community.updateCommunity(requestDto);
            communityRepository.save(community);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }


    }

    public void deleteCommunity(Long communityId, UserDetailsImpl userDetails) {
        String loginedUserId = userDetails.getUser().getUserid();
        String createdUserId = "";

        Community community = communityRepository.findByCommunityId(communityId);
        if (community != null) {
            createdUserId = community.getCreatedUser().getUserid();

            if (!loginedUserId.equals(createdUserId)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
            communityRepository.deleteById(communityId);
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
