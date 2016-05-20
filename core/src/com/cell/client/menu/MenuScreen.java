package com.cell.client.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cell.client.GameClient;
import com.cell.client.Main;
import com.cell.network.user.UserInfo;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by kongo on 17.03.16.
 */
public class MenuScreen implements Screen {
    private final GameClient gameClient;
    private Main main;
    private Stage stage;
    private Table rootTable;
    private WaitingWindow waitingWindow;
    private VisWindow currentMainWindow;

    public MenuScreen(Main main, GameClient gameClient) {
        this.main = main;
        this.gameClient = gameClient;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        openMainMenu();
    }

    public void openMainMenu() {
        rootTable.clearChildren();
        currentMainWindow = new MenuWindow(this);
        rootTable.add(currentMainWindow);
    }

    public void openLoginForm() {
        rootTable.clearChildren();
        rootTable.add(new LoginForm(this, gameClient));
    }

    public void openRegisterForm() {
        rootTable.clearChildren();
        rootTable.add(new RegisterForm(this, gameClient));
    }

    public void openUserWindow() {
        waitingWindow = null;
        rootTable.clearChildren();
        currentMainWindow = new UserWindow(this, gameClient);
        rootTable.add(currentMainWindow);
    }

    public void openEditForm(UserInfo userInfo) {
        rootTable.clearChildren();
        rootTable.add(new EditForm(this, gameClient, userInfo));
    }

    public void openWaitingWindow() {
        rootTable.clearChildren();
        waitingWindow = new WaitingWindow(this);
        rootTable.add(waitingWindow);
    }

    public void openCurrentMainWindow() {
        waitingWindow = null;
        rootTable.clearChildren();
        rootTable.add(currentMainWindow);
    }

    public void backFromGameScreen() {
        waitingWindow.close();
        createDailog("Game Over", "You Lost");
    }


    @Override
    public void render(float delta) {
        if (waitingWindow != null)
            waitingWindow.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void createDailog(String title, String text) {
        Dialogs.showOKDialog(stage, title, text);
    }

}
