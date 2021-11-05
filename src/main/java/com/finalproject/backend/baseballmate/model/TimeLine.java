package com.finalproject.backend.baseballmate.model;

import com.finalproject.backend.baseballmate.requestDto.TimeLineRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class TimeLine extends Timestamped
{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column()
    private Long id; // 게시글 고유 번호

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "createdUser")
    private User createdUser;// 게시글 작성자의 아이디, 중복 허용X

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String content; // 게시글 내용

    @OneToMany(mappedBy = "timeLine" ,cascade = CascadeType.ALL)
    private List<TimeLineLikes> likesList;

    @Column(columnDefinition = "integer default 0")
    private int likeCount;

    public void addLikes(TimeLineLikes like) {
        this.likesList.add(like);
        this.likeCount += 1;
    }

    public void deleteLikes(TimeLineLikes like) {
        this.likesList.remove(like);
        this.likeCount -= 1;
    }

    public TimeLine(User logineduser,TimeLineRequestDto requestDto)
    {
        this.userName = logineduser.getUsername();
        this.content = requestDto.getContent();
        this.createdUser = logineduser;
    }

}
