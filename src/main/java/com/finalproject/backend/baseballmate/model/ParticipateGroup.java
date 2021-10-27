package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ParticipateGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participatedUserId")
    private User participatedUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participatedGroup")
    private Group participatedGroupId;


}
