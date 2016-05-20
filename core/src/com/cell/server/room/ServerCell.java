package com.cell.server.room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.cell.client.game.player.Cell;
import com.cell.network.game.InputPackage;
import com.cell.server.GameConnection;

/**
 * Created by kongo on 05.04.16.
 */
public class ServerCell extends Cell {
    private int lastProcessedInput;
    private GameConnection connection;
    private Rectangle viewBounds;
    private float viewWidth = Gdx.graphics.getWidth();
    private float viewHeight = Gdx.graphics.getHeight();

    public ServerCell(GameConnection connection, float x, float y, int lastProcessedInput, float size, int color) {
        super(x, y, size, color);
        this.connection = connection;
        this.lastProcessedInput = lastProcessedInput;
        viewBounds = new Rectangle();
    }

    @Override
    public void applyInput(InputPackage input){
        super.applyInput(input);
        viewBounds.set(x - viewWidth / 2, y - viewHeight / 2, viewWidth, viewHeight);
    }

    public GameConnection getConnection() {
        return connection;
    }

    public int getLastProcessedInput() {
        return lastProcessedInput;
    }

    public void setLastProcessedInput(int lastProcessedInput) {
        this.lastProcessedInput = lastProcessedInput;
    }

    public Rectangle getViewBounds(){
        return viewBounds;
    }
}
