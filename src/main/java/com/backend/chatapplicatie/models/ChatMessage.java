package com.backend.chatapplicatie.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Entity
@Table(name = "public_messages")
@Data
@ToString
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 15)
    private String nickname;

    @Size(max = 100)
    private String content;
    private Date timestamp;
}
