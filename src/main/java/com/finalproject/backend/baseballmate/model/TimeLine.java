package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TimeLine extends Timestamped
{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column()
    private Long Id; // 게시글 고유 번호

    @Column(nullable = false)
    private String userName; // 게시글 작성자의 닉네임, 중복 허용X

    @Column(nullable = false)
    private String content; // 게시글 내용

    public TimeLine(String userName,TimeLineRequestDto requestDto)
    {
        this.userName = userName;
        this.content = requestDto.getContent();
    }

}
