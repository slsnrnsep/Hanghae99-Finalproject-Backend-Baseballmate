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
    private final UserService userService;

//    public User findAlarmUser(Authentication authentication){
//        return userService.findCurUser(authentication).orElseThrow(
//                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다")
//        );
//    }

    // 알람조회
   @Transactional
    public List<Alarm> getAlarm(User user) {

        List<Alarm> alarmList =  alarmRepository.findByUserIdOrderByCreatedAtDesc(user);

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
    public Map<String, String> deleteAlarm( UserDetailsImpl userDetails, Long alarmId) {
        Map<String, String> map = new HashMap<>();

        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new IllegalArgumentException("해당 알람이 존재하지 않습니다")
        );
        String loginUser = userDetails.getUser().getUserid();
        Long userId = alarm.getUserId();

        if(!loginUser.equals(userId)){
            throw new IllegalArgumentException("자신의 알람만 삭제할 수 있습니다");
        }
        alarmRepository.deleteById(alarmId);
        map.put("msg", "Success");
        return map;
    }
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
    public void alarmCreateUser(String commentAlarm, Group CreatedUser){
        AlarmRequestDto alarmRequestDto = new AlarmRequestDto();
        alarmRequestDto.setUserId(CreatedUser.getCreatedUser().getId());
        alarmRequestDto.setContents(commentAlarm);
        createAlarm(alarmRequestDto);
    }
}
