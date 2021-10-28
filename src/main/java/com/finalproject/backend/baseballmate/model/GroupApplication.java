package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class GroupApplication {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicatedUserId")
    private User applicatedUser; // 모임에 지원한 지원자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicatedGroupId")
    private Group applicatedGroup; // 지원자가 지원한 모임

    @Column
    private Boolean isAccepted; // 지원자의 모임 참가 확정 여부

    public GroupApplication(User user, Group group) {
        this.applicatedUser = user;
        this.applicatedGroup = group;
    }

    public void setUser(User user) {
        this.applicatedUser = user;
    }

    public void setGroup(Group group) {
        this.applicatedGroup = group;
    }

    public void setApplication(User user, Group group) {
        this.applicatedUser = user;
        this.applicatedGroup = group;
    }
}
