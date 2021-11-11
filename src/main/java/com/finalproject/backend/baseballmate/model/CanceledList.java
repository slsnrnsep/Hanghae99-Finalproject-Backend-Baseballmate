package com.finalproject.backend.baseballmate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class CanceledList {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceledUserId")
    private User canceledUser; // 취소했던 지원자

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canceledGroupId")
    private Group canceledGroup; // 취소했던 모임


    public CanceledList(User user, Group group) {
        this.canceledUser = user;
        this.canceledGroup = group;
    }
}
