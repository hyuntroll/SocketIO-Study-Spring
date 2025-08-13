package com.example.socketiostudyspring.socket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SocketIOLifeCycleTest {

    @Autowired
    private SocketIOLifeCycle lifeCycle;

    @Test
    void testSocketLifeCycle() {
        assertNotNull(lifeCycle);

    }

}