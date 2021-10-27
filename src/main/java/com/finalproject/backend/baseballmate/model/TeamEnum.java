package com.finalproject.backend.baseballmate.model;

public enum TeamEnum {
    NC("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_NC.png"),
    두산("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_OB.png"),
    KT("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_KT.png"),
    LG("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_LG.png"),
    키움("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_WO.png"),
    KIA("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_HT.png"),
    롯데("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_LT.png"),
    삼성("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_SS.png"),
    SSG("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_SK.png"),
    한화("https://lgcxydabfbch3774324.cdn.ntruss.com/KBO_IMAGE/emblem/regular/2021/emblem_HH.png");

    private final String value;

    TeamEnum(String value){ this.value = value; }

    public String getValue(){ return value; }

}
