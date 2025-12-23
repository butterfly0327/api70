package com.yumyumcoach.domain.ai.chatbot.mapper;

import com.yumyumcoach.domain.ai.chatbot.entity.AiChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiChatConversationMapper {
    int insertConversation(@Param("conversation") AiChatConversation conversation);

    AiChatConversation findByIdAndEmail(@Param("conversationId") Long conversationId,
                                        @Param("email") String email);
}
