package com.BackEnd.BidPro.chat.service;


import com.BackEnd.BidPro.chat.dto.request.MessageRequest;
import com.BackEnd.BidPro.chat.dto.response.MessagesResponse;

import java.util.List;


public interface ChatMessageService {
    void sendMessage(MessageRequest messageRequest);
    List<MessagesResponse> roomMessages(Long id);
}
