package com.finalproject.backend.baseballmate.model;

public enum TeamEnum {
    NC("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/NC.png&type=f64_64&refresh=1"),
    두산("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/OB.png&type=f64_64&refresh=1"),
    KT("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/KT.png&type=f64_64&refresh=1"),
    LG("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/LG.png&type=f64_64&refresh=1"),
    키움("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/WO.png&type=f64_64&refresh=1"),
    KIA("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/HT.png&type=f64_64&refresh=1"),
    롯데("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/LT.png&type=f64_64&refresh=1"),
    삼성("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/SS.png&type=f64_64&refresh=1"),
    SSG("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/SK.png&type=f64_64&refresh=1"),
    한화("https://dthumb-phinf.pstatic.net/?src=https://sports-phinf.pstatic.net/team/kbo/default/HH.png&type=f64_64&refresh=1");

    private final String value;

    TeamEnum(String value){ this.value = value; }

    public String getValue(){ return value; }

}
