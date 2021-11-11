package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CanceledScreenList {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceledUserId")
    private User canceledUser; // 취소했던 지원자

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceledScreenId")
    private Screen cancledScreen; // 취소했던 스야 모임

    public CanceledScreenList(User user, Screen screen){
        this.canceledUser = user;
        this.cancledScreen = screen;
    }
}
