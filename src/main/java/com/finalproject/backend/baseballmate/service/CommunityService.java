package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Community;
import com.finalproject.backend.baseballmate.model.CommunityComment;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.CommunityCommentRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private String commonPath = "/images"; // 파일 저장할 기본 경로 변수 설정, 초기화
    private final CommunityCommentRepository communityCommentRepository;

    @Transactional
    public Community createCommunity(User loginedUser, CommunityRequestDto requestDto) {
        String communityUserPicture = loginedUser.getPicture();
        String myTeam = loginedUser.getMyselectTeam();
        Community community = new Community(loginedUser, requestDto, communityUserPicture, myTeam);
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
            String title = community.getTitle();
            String content = community.getContent();
            String communityUserPicture = community.getCommunityUserPicture();
            String filePath = community.getFilePath();
            String myTeam = community.getMyTeam();

            AllCommunityDto communityDto =
                    new AllCommunityDto(communityId, userName, title, content, communityUserPicture, filePath, myTeam);
            data.add(communityDto);
        }
        return data;
    }

    public CommunityDetailResponseDto getCommunityDetail(Long communityId) {
        Community community = communityRepository.findById(communityId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 아이디입니다")
        );

        String userName = community.getUserName();
        String title = community.getTitle();
        String content = community.getContent();
        String communityUserPicture = community.getCommunityUserPicture();
        String filePath = community.getFilePath();
        String myTeam = community.getMyTeam();
        List<CommunityComment> communityCommentList = communityCommentRepository.findAllByCommunity_CommunityIdOrderByModifiedAtDesc(communityId);

        CommunityDetailResponseDto communityDetailResponseDto =
                new CommunityDetailResponseDto(userName, title, content, communityUserPicture, filePath, myTeam, communityCommentList);
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
}
