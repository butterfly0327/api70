package com.yumyumcoach.domain.title.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMyTitleRequest {
    private Long titleId; // null 허용하면 “대표 타이틀 해제”도 가능
}
