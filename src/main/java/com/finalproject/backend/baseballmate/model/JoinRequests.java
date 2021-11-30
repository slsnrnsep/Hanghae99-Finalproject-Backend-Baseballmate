package com.finalproject.backend.baseballmate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long ownUserId;

    @Column(nullable = false)
    private String grouptype;

    public JoinRequests(Long userId,Long postId,Long ownUserId,String grouptype) {
        this.userId = userId;
        this.postId = postId;
        this.ownUserId = ownUserId;
        this.grouptype = grouptype;
    }

}