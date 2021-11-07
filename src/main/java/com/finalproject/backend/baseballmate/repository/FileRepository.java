package com.finalproject.backend.baseballmate.repository;

import com.finalproject.backend.baseballmate.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
