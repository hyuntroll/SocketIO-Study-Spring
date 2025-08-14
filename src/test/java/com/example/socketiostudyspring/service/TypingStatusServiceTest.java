package com.example.socketiostudyspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TypingStatusServiceTest {

    @Autowired
    private TypingStatusService service;

    @Test
    public void testTyping() {
        service.updateTying("abc-abc-abc");
        if (service.isTyping("abc-abc-abc")) {
            System.out.println("있음");
        }
        else {
            System.out.println("없음");
        }
    }

}