package com.cell.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.cell.client.game.GameScreen;
import com.cell.client.game.chat.ChatController;
import com.cell.client.game.chat.ChatWindow;
import com.cell.client.menu.MenuScreen;
import com.esotericsoftware.kryonet.Client;
import com.kotcrab.vis.ui.VisUI;

public class Main extends Game {
    private Client client;

    private GameClient gameClient;
    private ChatWindow chatWindow;
    private ChatController chatController;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    @Override
    public void create() {
        System.out.println(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
        VisUI.load();

        client = new Client();
        chatWindow = new ChatWindow(client);
        chatController = new ChatController(chatWindow);

        gameClient = new GameClient(this, client, chatController);
        gameClient.connect();

        menuScreen = new MenuScreen(this, gameClient);
        gameScreen = new GameScreen(this, gameClient, chatWindow, chatController);
        setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public void setMenuScreen() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(menuScreen);

            }
        });
    }

    public void setGameScreen(int worldWidth, int worldHeinght) {
        gameScreen.setWorldWidth(worldWidth);
        gameScreen.setWorldHeight(worldHeinght);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(gameScreen);
            }
        });
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
