package com.finalproject.backend.baseballmate.responseDto;

import com.finalproject.backend.baseballmate.requestDto.AllScreenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MypageResponseDto {

    private List<AllGroupResponseDto> myGroupResponseDtoList;
    private List<AllScreenResponseDto> myScreenResponseDtoList;

}
