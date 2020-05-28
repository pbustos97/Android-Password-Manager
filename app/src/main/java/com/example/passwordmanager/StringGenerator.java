package com.example.passwordmanager;

import java.util.Random;

public class StringGenerator {
    private String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String integers = "1234567890";
    private String symbols = "!@#$%^&*";
    private boolean useSymbols = true;
    private Random random = new Random();

    public void setUseSymbols(boolean toggle) {
        useSymbols = toggle;
    }

    public String generatedString(long seed, int length) {
        if (length < 8) {
            return "failed";
        }
        random.setSeed(seed);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (useSymbols == true) {
            String charSeed = characters + integers + symbols;
            while (i < length) {
                sb.append(charSeed.charAt(random.nextInt(charSeed.length())));
                i++;
            }
        } else {
            String charSeed = characters + integers;
            while (i < length) {
                sb.append(charSeed.charAt(random.nextInt(charSeed.length())));
                i++;
            }
        }
        return sb.toString();
    }
}
