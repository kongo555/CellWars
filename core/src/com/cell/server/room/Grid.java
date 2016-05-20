package com.cell.server.room;

import com.badlogic.gdx.math.Vector2;
import com.cell.client.game.player.Cell;

import java.util.ArrayList;

/**
 * Created by kongo on 08.04.16.
 */
public class Grid {
    public int cellSize = 2;
    public int width, height;
    public ArrayList<Cell>[] entitiesInCell;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        entitiesInCell = new ArrayList[(width / cellSize) * (height / cellSize)];
        for (int i = 0; i < (width / cellSize) * (height / cellSize); i++) {
            entitiesInCell[i] = new ArrayList<Cell>();
        }
    }

    public void insertEntity(Cell cell, int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return;
        entitiesInCell[x + y * (width / cellSize)].add(cell);
    }

    public void removeEntity(Cell cell, int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return;
        entitiesInCell[x + y * (width / cellSize)].remove(cell);
    }

    public void move(Cell cell, Vector2 oldPosition, Vector2 position) {
        // See which cell it was in.
        int oldCellX = (int) (oldPosition.x / cellSize);
        int oldCellY = (int) (oldPosition.y / cellSize);

        // See which cell it's moving to.
        int cellX = (int) (position.x / cellSize);
        int cellY = (int) (position.y / cellSize);

        // If it didn't change cells, we're done.
        if (oldCellX == cellX && oldCellY == cellY)
            return;

        // Unlink it from the list of its old cell.
        removeEntity(cell, oldCellX, oldCellY);

        // Add it back to the grid at its new cell.
        insertEntity(cell, cellX, cellY);
    }

    public ArrayList<Cell> getEntities(Vector2 position) {
        ArrayList<Cell> entities = new ArrayList<Cell>();

        int cellX = (int) (position.x / cellSize);
        int cellY = (int) (position.y / cellSize);

        entities.addAll(entitiesInCell[cellX + cellY * (width / cellSize)]);
        if (position.x > 0 && position.y > 0)
            entities.addAll(entitiesInCell[cellX - 1 + (cellY - 1) * (width / cellSize)]);
        if (position.x > 0)
            entities.addAll(entitiesInCell[cellX - 1 + cellY * (width / cellSize)]);
        if (position.y > 0)
            entities.addAll(entitiesInCell[cellX + (cellY - 1) * (width / cellSize)]);
        if (position.x > 0 && position.y < (height / cellSize) - 1)
            entities.addAll(entitiesInCell[cellX - 1 + (cellY + 1) * (width / cellSize)]);

        return entities;
    }

    /*public ArrayList<Entity> getEntitiesInRect(Rectangle rect) {
        ArrayList<Entity> result = new ArrayList<Entity>();
        int xt0 = (int) (rect.x / cellSize);
        int yt0 = (int) (rect.y / cellSize);
        int xt1 = (int) ((rect.x + rect.width) / cellSize);
        int yt1 = (int) ((rect.y + rect.height) / cellSize);
        for (int y = yt0; y <= yt1; y++) {
            for (int x = xt0; x <= xt1; x++) {
                if (x < 0 || y < 0 || x >= width / cellSize || y >= height / cellSize)
                    continue;
                for (Entity entity : entitiesInCell[x + y * (width / cellSize)]) {
                    Rectangle entityRect = entity.getComponent(Bounds.class).rect;
                    if (rect.overlaps(entityRect))
                        result.add(entity);
                }
            }
        }
        return result;
    }*/


}

