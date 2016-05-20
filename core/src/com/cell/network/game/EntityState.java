package com.cell.network.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by kongo on 04.04.16.
 */
public class EntityState {
    public float x;
    public float y;
    public float size;
    public int color;
    //public int last_processed_input;

    public EntityState(){}

    public EntityState(float x, float y, float size, int color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }
}
