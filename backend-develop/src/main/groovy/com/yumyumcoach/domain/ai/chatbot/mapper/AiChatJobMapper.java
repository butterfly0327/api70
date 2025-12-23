package com.yumyumcoach.domain.ai.chatbot.mapper;

import com.yumyumcoach.domain.ai.chatbot.dto.ChatJobDetail;
import com.yumyumcoach.domain.ai.chatbot.dto.ChatJobStatusView;
import com.yumyumcoach.domain.ai.chatbot.entity.AiChatJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiChatJobMapper {
    int insertJob(@Param("job") AiChatJob job);

    ChatJobDetail findDetailById(@Param("jobId") Long jobId);

    ChatJobStatusView findStatusByIdAndEmail(@Param("jobId") Long jobId, @Param("email") String email);

    int updateJobStatus(@Param("jobId") Long jobId,
                        @Param("status") String status,
                        @Param("errorMessage") String errorMessage);
}
