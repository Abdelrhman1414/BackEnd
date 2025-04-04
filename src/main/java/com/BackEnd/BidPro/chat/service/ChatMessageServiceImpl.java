package com.BackEnd.BidPro.chat.service;

import com.BackEnd.BidPro.Model.User;
import com.BackEnd.BidPro.Repo.UserRepository;
import com.BackEnd.BidPro.chat.dto.request.MessageRequest;
import com.BackEnd.BidPro.chat.dto.response.MessagesResponse;
import com.BackEnd.BidPro.chat.model.ChatMessage;
import com.BackEnd.BidPro.chat.model.Room;
import com.BackEnd.BidPro.chat.repository.ChatMessageRepository;
import com.BackEnd.BidPro.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
    private final SimpMessagingTemplate template;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Override
    public void sendMessage(MessageRequest messageRequest) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(messageRequest.getMessage());
        chatMessage.setMessageDate(new Date());
        Room room = roomRepository.findById(messageRequest.getRoomId()).get();
        chatMessage.setRoom(room);
        User user = userRepository.findById(messageRequest.getUserId()).get();
        chatMessage.setSender(user);
        for(int i=0;i<room.getUsers().size();i++){
            String userId = String.valueOf(room.getUsers().get(i).getId());
            log.info("sending WS to {} with payload {} with current id : {}", userId, chatMessage.getMessage(),user.getId());
            template.convertAndSendToUser(
                    userId,
                    "/chat",
                    chatMessage.getMessage()
            );
        }
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<MessagesResponse> roomMessages(Long id) {
        List<ChatMessage> Messages = chatMessageRepository.findSortedMessage(id);
        List<MessagesResponse> messageResponses = new ArrayList<>();
        for(ChatMessage chatMessage : Messages){
            MessagesResponse messageResponse = new MessagesResponse();
            messageResponse.setMessage(chatMessage.getMessage());
            messageResponse.setUserId(chatMessage.getSender().getId());
            messageResponse.setDate(chatMessage.getMessageDate());
            messageResponses.add(messageResponse);
        }
        return messageResponses;

    }


}
