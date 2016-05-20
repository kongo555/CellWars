package com.cell.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.cell.server.database.DataBase;
import com.cell.server.room.Room;
import com.cell.server.view.ServerView;
import com.kotcrab.vis.ui.VisUI;

import java.io.IOException;

/**
 * Created by kongo on 17.03.16.
 */
public class Main extends ApplicationAdapter {
    private DataBase dataBase;
    private GameServer gameServer;
    private Room room;

    private ServerView serverView;


    @Override
    public void create() {
        VisUI.load();

        dataBase = new DataBase();
        try {
            gameServer = new GameServer(this, dataBase, room);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverView = new ServerView(dataBase);
    }

    private void update() {
        if (room != null)
            room.sendWorldState();
    }


    @Override
    public void render() {
        //update();
        if (room != null)
            room.update(Gdx.graphics.getDeltaTime());

        serverView.render();
    }

    @Override
    public void resize(int width, int height) {
        serverView.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dataBase.dispose();
        gameServer.dispose();
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
