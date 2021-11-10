package com.finalproject.backend.baseballmate.chat;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BanUserList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private Long userId;

    @Builder
    public BanUserList(Long roomId,Long userId){
        this.roomId = roomId;
        this.userId = userId;
    }