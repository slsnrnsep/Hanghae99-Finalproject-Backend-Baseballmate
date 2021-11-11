package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.CanceledScreenList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanceledScreenListRepository extends JpaRepository<CanceledScreenList, Long> {
    List<CanceledScreenList> findAllByCancledScreen_ScreenId(Long screenId);
}
