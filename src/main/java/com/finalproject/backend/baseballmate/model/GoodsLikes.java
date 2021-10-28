package com.finalproject.backend.baseballmate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GoodsLikes {
    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "goods_id")
    @ManyToOne
    private Goods goods;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public GoodsLikes(Goods goods, User user){
        this.goods = goods;
        this.user = user;
    }
}
