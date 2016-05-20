package com.cell.server.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cell.server.database.DataBase;

/**
 * Created by kongo on 14.04.16.
 */
public class ServerView {
    private Stage stage;
    private Table rootTable;
    private DataBase dataBase;

    public ServerView(DataBase dataBase){
        this.dataBase = dataBase;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        //stage.setDebugAll(true);

        openOptionsWindow();
    }

    public void render(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void openOptionsWindow(){
        rootTable.clearChildren();
        OptionsWindow optionsWindow = new OptionsWindow(this);
        rootTable.add(optionsWindow);
    }

    public void openStatsWindow(){
        rootTable.clearChildren();
        rootTable.add(new StatsWindow(this, dataBase)).width(300).fill().expand();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
