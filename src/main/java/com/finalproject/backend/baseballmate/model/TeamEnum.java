package com.finalproject.backend.baseballmate.model;

public enum TeamEnum {
    NC("https://www.thesportsdb.com/images/media/team/badge/6gwcg81589708218.png"),
    두산("https://www.thesportsdb.com/images/media/team/badge/e96s4z1589709054.png"),
    KT("https://www.thesportsdb.com/images/media/team/badge/qk8erg1589709962.png"),
    LG("https://www.thesportsdb.com/images/media/team/badge/vvii3b1589708608.png"),
    키움("https://www.thesportsdb.com/images/media/team/badge/qcj18p1589709259.png"),
    KIA("https://www.thesportsdb.com/images/media/team/badge/pesj9z1589709516.png"),
    롯데("https://www.thesportsdb.com/images/media/team/badge/l9quje1589708840.png"),
    삼성("https://www.thesportsdb.com/images/media/team/badge/5u6k511589709673.png"),
    SSG("https://www.thesportsdb.com/images/media/team/badge/49cfnl1623632712.png"),
    한화("https://www.thesportsdb.com/images/media/team/badge/u5t0x01589709824.png");

    private final String value;

    TeamEnum(String value){ this.value = value; }

    public String getValue(){ return value; }

}
