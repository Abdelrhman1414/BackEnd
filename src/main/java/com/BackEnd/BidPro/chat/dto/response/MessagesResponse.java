package com.BackEnd.BidPro.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagesResponse {
    private String message;
    private Long userId;
    private String date;
    private String senderName;
    private String senderPhoto;
}
