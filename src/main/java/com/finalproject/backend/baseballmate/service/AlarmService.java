package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.Alarm;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupApplication;
import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.AlarmRepository;
import com.finalproject.backend.baseballmate.requestDto.AlarmRequestDto;
import com.finalproject.backend.baseballmate.requestDto.AlarmSaveDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
//    private final UserService userService;

//    public User findAlarmUser(Authentication authentication){
//        return userService.findCurUser(authentication).orElseThrow(
//                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다")
//        );
//    }

    // 알람조회
   @Transactional
    public List<Alarm> getAlarm(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        List<Alarm> alarmList =  alarmRepository.findByUserIdOrderByCreatedAtDesc(userId);
//        alarmList.get(i).getCreatedAt();
        for(Alarm alarm : alarmList){
            AlarmSaveDto alarmSaveDto = new AlarmSaveDto();
            alarmSaveDto.setAlarmStatus(true);
            alarm.updateAlarm(alarmSaveDto);
        }
        return alarmList;
    }

    // 알람 생성
    @Transactional
    public void createAlarm(AlarmRequestDto alarmRequestDto){
       Alarm alarm = new Alarm(alarmRequestDto);
       alarmRepository.save(alarm);
    }



    // 알람 삭제
    public void deleteAlarm(UserDetailsImpl userDetails, Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new IllegalArgumentException("해당 알람이 존재하지 않습니다")
        );

        Long loginUser = userDetails.getUser().getId();
        Long userId = alarm.getId();


        if(alarm != null){
            userId = alarm.getUserId();

            if(!loginUser.equals(userId)){
                throw new IllegalArgumentException("삭제권한이 없습니다");
            }
            alarmRepository.deleteById(alarmId);
        } else {
            throw new NullPointerException("해당 게시글이 존재하지 않습니다.");
        }
    }

    // 알람 삭제 2
//    public Map<String, String> deleteAlarm(UserDetailsImpl userDetails, Long alarmId) {
//        Map<String, String> map = new HashMap<>();
//
//        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
//                () -> new IllegalArgumentException("해당 알람이 존재하지 않습니다")
//        );
//
//        String loginUser = userDetails.getUser().getUserid();
//        String userId = "";
//
//
//        if(alarm != null){
//            userId = alarm.getUserId().toString();
//
//            if(!loginUser.equals(userId)){
//                throw new IllegalArgumentException("삭제권한이 없습니다");
//            }
//        }
////        if(!loginUser.equals(userId)){
////            throw new IllegalArgumentException("자신의 알람만 삭제할 수 있습니다");
////        }
//        alarmRepository.deleteById(alarmId);
//        map.put("msg", "Success");
//        return map;
//    }


    // 읽지 않은 알람 카운드
    public Integer unreadAlarm(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return  alarmRepository.countAlarmByUserIdAndAlarmStatus(userId, false);
   }

   // 알람메이커 모임 참여자
   public void alarmAppliedUser(String commentAlarm, Group group, User user){
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(group.getCreatedUser().getId());
        alarmRequestDto.setContents(commentAlarm);
        if(!user.getId().equals(group.getCreatedUser().getId())){
            createAlarm(alarmRequestDto);
        }
   }

   // 알람메이커 모임 생성자
    public void alarmCreateUser(String commentAlarm, Group CreatedUser
    ){
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(CreatedUser.getCreatedUser().getId());
        alarmRequestDto.setContents(commentAlarm);
        createAlarm(alarmRequestDto);
    }

    public void alarmMethod(User alarmuser,String target,String title,String type,String msg,Long postid){
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        String signupAlarm = alarmuser.getUsername() + "님! "+target+" 님께서  작성한 "+type +" : '" +title+"'에 "+msg;
        alarmRequestDto.setUserId(alarmuser.getId());
        alarmRequestDto.setContents(signupAlarm);
        alarmRequestDto.setPostId(postid);
        alarmRequestDto.setAlarmType("Normal");
        createAlarm(alarmRequestDto);
    }

//    public Map<String, Object> deleteAlarm(User user) {
//       alarmRepository
//    }
}
