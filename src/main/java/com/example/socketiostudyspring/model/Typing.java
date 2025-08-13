package com.example.socketiostudyspring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Typing {
    private String userId;
    private boolean typing;
}
