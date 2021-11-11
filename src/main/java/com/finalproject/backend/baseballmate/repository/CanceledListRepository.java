package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CanceledList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanceledListRepository extends JpaRepository<CanceledList, Long> {
    List<CanceledList> findAllByCanceledGroup_GroupId(Long groupId);
}
