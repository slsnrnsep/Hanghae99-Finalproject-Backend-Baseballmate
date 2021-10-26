package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.model.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AllGroupResponseDto {
    private String status;
    private List<Group> allGroup;
}
