package com.yumyumcoach.domain.title.service;

import com.yumyumcoach.domain.title.dto.*;
import com.yumyumcoach.domain.title.mapper.TitleMapper;
import com.yumyumcoach.domain.user.mapper.ProfileMapper;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TitleService {

    private final TitleMapper titleMapper;
    private final ProfileMapper profileMapper;

    public MyTitleResponse getMyCurrentTitle(String email) {
        return titleMapper.findCurrentTitle(email);
    }

    public List<MyTitleItemResponse> getMyTitles(String email) {
        return titleMapper.findMyTitles(email);
    }

    @Transactional
    public MyTitleResponse updateMyCurrentTitle(String email, UpdateMyTitleRequest request) {
        Long titleId = request.getTitleId();

        if (titleId != null && !titleMapper.ownsTitle(email, titleId)) {
            throw new BusinessException(ErrorCode.TITLE_FORBIDDEN);
        }

        profileMapper.updateDisplayTitle(email, titleId);
        return titleMapper.findCurrentTitle(email);
    }
}
