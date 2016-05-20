package com.cell.utils;

/**
 * Created by kongo on 30.03.16.
 */
public class HashGenerationException extends Exception {
    public HashGenerationException(String s) {
        super(s);
    }
    public HashGenerationException(String s, Exception ex) {
        super(s, ex);
    }
}
