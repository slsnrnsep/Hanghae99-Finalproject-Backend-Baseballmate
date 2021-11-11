package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ScreenApplication {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliedUserId")
    private User appliedUser; // 모임에 지원한 지원자

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliedScreenId")
    private Screen appliedScreen; // 지원자가 지원한 모임

    @Column
    private Boolean isAccepted; // 지원자의 모임 참가 확정 여부

    public ScreenApplication(User user, Screen group) {
        this.appliedUser = user;
        this.appliedScreen = group;
    }
}
