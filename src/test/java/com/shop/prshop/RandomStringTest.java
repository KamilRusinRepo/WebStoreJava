package com.shop.prshop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomStringTest {
    @Test
    void generate_ShouldReturnStringOfRequestedLength() {
        int length = 15;

        String result = RandomString.generate(length);

        assertEquals(length, result.length());
    }
}
