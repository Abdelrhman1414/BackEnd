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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        ZonedDateTime now = ZonedDateTime.now();
        chatMessage.setMessageDate(now);
        Room room = roomRepository.findById(messageRequest.getRoomId()).get();
        chatMessage.setRoom(room);
        User user = userRepository.findById(messageRequest.getUserId()).get();
        chatMessage.setSender(user);
        for(int i=0;i<room.getUsers().size();i++) {
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
    public List<MessagesResponse> roomMessages(Long roomId) {
        List<ChatMessage> Messages = chatMessageRepository.findSortedMessage(roomId);
        List<MessagesResponse> messageResponses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(ChatMessage chatMessage : Messages){
            ZonedDateTime time;
            MessagesResponse messageResponse = new MessagesResponse();
            messageResponse.setMessage(chatMessage.getMessage());
            messageResponse.setUserId(chatMessage.getSender().getId());
            if(chatMessage.getSender().getRole().name().equals("ADMIN")){
                messageResponse.setSenderName("Admin");
            }
            else {
                messageResponse.setSenderName(chatMessage.getSender().getName());
            }
            if (chatMessage.getSender().getImage()!= null) {
                messageResponse.setSenderPhoto(chatMessage.getSender().getImage().getUrl());
            }
            else {
                messageResponse.setSenderPhoto(null);
            }
            time = chatMessage.getMessageDate().toInstant().atZone(ZoneId.of("Africa/Cairo"));
            messageResponse.setDate(time.format(formatter));
            messageResponses.add(messageResponse);
        }
        return messageResponses;

    }


}
