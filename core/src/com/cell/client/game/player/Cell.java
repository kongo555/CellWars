package com.cell.client.game.player;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.cell.network.game.InputPackage;

/**
 * Created by kongo on 02.04.16.
 */
public class Cell extends InputAdapter {
    protected float x;
    protected float y;
    protected float size;
    private float speed;
    private int color;

    public Cell(float x, float y, float size, int color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        speed = 100;
    }

    public void applyInput(InputPackage input){
        x += input.pressTimeHorizontal * this.speed;
        y += input.pressTimeVertical * this.speed;
    }

    public void render(ShapeRenderer shapeRenderer){
        switch (color) {
            case 0:
                shapeRenderer.setColor(Color.ROYAL);
                break;
            case 1:
                shapeRenderer.setColor(Color.CYAN);
                break;
            case 2:
                shapeRenderer.setColor(Color.ORANGE);
                break;
            case 3:
                shapeRenderer.setColor(Color.PURPLE);
                break;
        }

        shapeRenderer.circle(x, y, size);
    }

    public String toString(){
        return x + " " + y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
