package com.pedidosya.test.review;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Review {
    private String id;
    private String userId;
    private String shopId;
    private String purchaseId;
    private LocalDateTime timestamp;

    private String comment;
    private int score;
}
