package com.yumyumcoach.domain.ai.chatbot.mapper;

import com.yumyumcoach.domain.ai.chatbot.entity.AiChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiChatMessageMapper {
    int insertMessage(@Param("message") AiChatMessage message);

    int updateAssistantMessage(@Param("messageId") Long messageId,
                               @Param("status") String status,
                               @Param("content") String content,
                               @Param("errorMessage") String errorMessage);

    List<AiChatMessage> findByConversation(@Param("conversationId") Long conversationId,
                                           @Param("email") String email);
}
