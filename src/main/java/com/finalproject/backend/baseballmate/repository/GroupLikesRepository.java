package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.GoodsLikes;
import com.finalproject.backend.baseballmate.model.Group;
import com.finalproject.backend.baseballmate.model.GroupLikes;
import com.finalproject.backend.baseballmate.model.TimeLineLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupLikesRepository extends JpaRepository<GroupLikes,Long> {
    Optional<GroupLikes> findByGrouplikesGroupIdAndUserId(Long groupId, Long UserId);
    List<GroupLikes> findAllByUserId(Long UserId);
}
