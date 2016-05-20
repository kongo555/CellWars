package com.cell.server.room;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.cell.client.game.player.Cell;
import com.cell.network.game.EntityState;
import com.cell.network.game.PlayerState;
import com.cell.network.game.WorldState;
import com.cell.server.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kongo on 08.04.16.
 */
public class Room {
    private GameServer gameServer;
    private int worldWidth = 1000;
    private int worldHeight = 1000;
//    private HashMap<Integer, ServerCell> entities;
    private ConcurrentHashMap<Integer, ServerCell> entities;
    private ArrayList<Cell> foods;
    private float delay = 0.1f;
    private float time = 0;
    public static final float startCellSize = 30;
    public static final float startFoodSize = 12;

    public Room(GameServer gameServer){
        this.gameServer = gameServer;
        entities = new ConcurrentHashMap<Integer, ServerCell>();
        foods = new ArrayList<Cell>(100);
        for (int i = 0; i < 100; i++) {
            foods.add(new Cell(MathUtils.random(worldWidth), MathUtils.random(worldHeight), startFoodSize, MathUtils.random(3)));
        }
    }

    public void update(float delta){
        for (Map.Entry<Integer, ServerCell> entry : entities.entrySet()) {
            //entry.
        }

        sendWorldState();

        time+= delta;
        if(time >= delay){
            time = 0;
        }
    }

    public void sendWorldState() {
        // Gather the state of the world. In a real app, state could be filtered to
        // avoid leaking data (e.g. position of invisible enemies).
        EntityState[] states = new EntityState[entities.size() + foods.size()];
        int i = 0;
        for (Map.Entry<Integer, ServerCell> entry : entities.entrySet()) {
            ServerCell entity = entry.getValue();
            states[i] = new EntityState(entity.getX(), entity.getY(), entity.getSize(), entity.getColor());
            i++;
        }
        for (Cell food: foods) {
            states[i] = new EntityState(food.getX(), food.getY(), food.getSize(), food.getColor());
            i++;
        }

        for (Map.Entry<Integer, ServerCell> entry : entities.entrySet()) {
            ServerCell entity = entry.getValue();
            PlayerState playerState = new PlayerState(entity.getX(), entity.getY(), entity.getLastProcessedInput());
            entity.getConnection().sendTCP(new WorldState(playerState, states));
        }
        // Broadcast the state to all the clients.
    }

    public void sendWorldState2(){
        for (Map.Entry<Integer, ServerCell> entry : entities.entrySet()) {
            ServerCell entity = entry.getValue();
            entity.getConnection().sendTCP(playerWorldState(entity));
        }
    }

    public WorldState playerWorldState(ServerCell entity){
        ArrayList<EntityState> entityStates = new ArrayList<EntityState>();
        int i = 0;
        for (Map.Entry<Integer, ServerCell> entry : entities.entrySet()) {
            ServerCell otherEntity = entry.getValue();
            if(entity.getViewBounds().contains(otherEntity.getX(), otherEntity.getY()) && (entity.getConnection().getID() != otherEntity.getConnection().getID()))
                entityStates.add(new EntityState(entity.getX(), entity.getY(), entity.getSize(), entity.getColor()));
        }
        for (Cell food: foods) {
            if(entity.getViewBounds().contains(food.getX(), food.getY()))
                entityStates.add(new EntityState(entity.getX(), entity.getY(), entity.getSize(), entity.getColor()));
        }

        PlayerState playerState = new PlayerState(entity.getX(), entity.getY(), entity.getLastProcessedInput());
        EntityState[] states = entityStates.toArray(new EntityState[0]);
        return new WorldState(playerState, states);
    }

    public void checkCollision(ServerCell entity){
        Circle cellCircle = new Circle();
        cellCircle.set(entity.getX(), entity.getY(), entity.getSize());
        Circle otherCircle = new Circle();
        for (int i = 0; i < foods.size(); i++) {
            Cell food = foods.get(i);
            otherCircle.set(food.getX(), food.getY(), food.getSize());
            if(cellCircle.overlaps(otherCircle)) {
                foods.remove(i);
                i--;
                entity.setSize(entity.getSize() + 1);
            }
        }

        for(Iterator<Map.Entry<Integer, ServerCell>> it = entities.entrySet().iterator(); it.hasNext(); ){
            Map.Entry<Integer, ServerCell> entry = it.next();
            ServerCell otherCell = entry.getValue();
            if (entity == otherCell)
                continue;
            otherCircle.set(otherCell.getX(), otherCell.getY(), otherCell.getSize());

            if(cellCircle.radius != otherCircle.radius) {
                float dx = cellCircle.x - otherCircle.x;
                float dy = cellCircle.y - otherCircle.y;
                float distance = dx * dx + dy * dy;

                if (distance < cellCircle.radius * cellCircle.radius && cellCircle.radius > otherCircle.radius + 5) {
                    entity.setSize(entity.getSize() + otherCell.getSize());
                    gameServer.removePlayer(otherCell);
                    it.remove();
                }
                else if (distance < otherCircle.radius * otherCircle.radius && otherCircle.radius > cellCircle.radius + 5) {
                    //FIXME !
                    otherCell.setSize(entity.getSize() + otherCell.getSize());
                    gameServer.removePlayer(entity);
                    entities.remove(entity.getConnection().getID());
                }
            }
        }
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Map<Integer, ServerCell> getEntities() {
        return entities;
    }
}
