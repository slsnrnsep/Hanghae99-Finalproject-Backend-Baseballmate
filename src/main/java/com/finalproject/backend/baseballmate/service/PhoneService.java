package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.model.User;
import com.finalproject.backend.baseballmate.repository.UserRepository;
import com.finalproject.backend.baseballmate.requestDto.PhoneRequstDto;
import com.finalproject.backend.baseballmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
//@RequiredArgsConstructor

public class PhoneService {

    private final PhoneService phoneService;
    private final UserRepository userRepository;

    public PhoneService(@Lazy UserRepository userRepository,@Lazy PhoneService phoneService){
        this.userRepository = userRepository;
        this.phoneService = phoneService;
    }


    private static int  creatkey(){
        int ranNum = (int)(Math.random() * (99999 - 10000 + 1)) + 10000;
        System.out.println(ranNum);
        return ranNum;
    }

    public int  sendMessage(PhoneRequstDto requstDto) {
//        String api_key = "NCSEJJ3UAETQNIRX";
//        String api_secret = "YSQKW2V1YETXYWBZGDWNZSGFLZROFAJV";

        String api_key = "NCSJGLZSHFQHDBUW" ;
        String api_secret = "OV3SDSM1OFTZEFBZSOSRXZL9CO8YO8DC";

        Message coolsms = new Message(api_key, api_secret);
        String phoneNumber = requstDto.getPhoneNumber();
        int ranNum = creatkey();
        // 4 params(to, from, type, text) are mandatory. must be filled

        HashMap<String, String> params = new HashMap<>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "07080940569");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨

        params.put("type", "SMS");
        params.put("text", "[MEETBALL 회원가입]\n 인증번호는: " + "["+ranNum+"]" +"입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

            if(obj.containsKey("error")){
                throw new IllegalArgumentException("문자메세지가 발송되지 못했습니다.");
           }
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
        String userid = "test"+ranNum;
        User user = new User(userid,"테스트닉네임","a123123!",phoneNumber+"#"+ranNum,ranNum);
        userRepository.save(user);

        return ranNum;
    }

    public void CustomsendMessage(String textmsg,User user) {
//        String api_key = "NCSEJJ3UAETQNIRX";
//        String api_secret = "YSQKW2V1YETXYWBZGDWNZSGFLZROFAJV";

        String api_key = "NCSJGLZSHFQHDBUW" ;
        String api_secret = "OV3SDSM1OFTZEFBZSOSRXZL9CO8YO8DC";
        Message coolsms = new Message(api_key, api_secret);
        String phoneNumber = user.getPhoneNumber();

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "07080940569");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", textmsg);
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

            if(obj.containsKey("error")){
                throw new IllegalArgumentException("문자메세지가 발송되지 못했습니다.");
            }
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

    public void confirmNumChk(PhoneRequstDto requstDto) {

        User user = userRepository.findByPhoneNumber(requstDto.getPhoneNumber()+"#"+requstDto.getRanNum()).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 일치하지 않습니다")
        );

        int userRanNum = user.getRanNum();
        if(requstDto.getRanNum() == (userRanNum))
        {
            System.out.println("인증번호가 일치합니다");
        }else{
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다");
        }
    }
}
