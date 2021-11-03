package com.finalproject.backend.baseballmate.service;

import com.finalproject.backend.baseballmate.requestDto.PhoneRequstDto;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PhoneService {

    public void sendMessage(PhoneRequstDto requstDto, String ranNum) {
        String api_key = "NCSEJJ3UAETQNIRX";
        String api_secret = "YSQKW2V1YETXYWBZGDWNZSGFLZROFAJV";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<>();
        params.put("to", requstDto.getPhoneNumber());    // 수신전화번호
        params.put("from", "01090029710");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "인증번호 : " + "["+ranNum+"]");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }

}
