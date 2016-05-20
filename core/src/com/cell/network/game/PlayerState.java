package com.cell.network.game;

/**
 * Created by kongo on 14.04.16.
 */
public class PlayerState {
    public float x;
    public float y;
    public int last_processed_input;

    public PlayerState(){}

    public PlayerState(float x, float y, int last_processed_input) {
        this.x = x;
        this.y = y;
        this.last_processed_input = last_processed_input;
    }
}

