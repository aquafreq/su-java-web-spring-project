package com.example.english.data.entity.enumerations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum  LevelOfLanguage {
    A1, A2, B1, B2, C1, C2;

    private static final List<LevelOfLanguage> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static LevelOfLanguage randomLevel()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
