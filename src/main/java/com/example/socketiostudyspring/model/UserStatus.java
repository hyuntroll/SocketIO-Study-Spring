package com.example.socketiostudyspring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더에서 생성자를 사용함
public class UserStatus {
    private String userId;
    private String connectStatus;
}
