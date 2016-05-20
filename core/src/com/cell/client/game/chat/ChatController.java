package com.cell.client.game.chat;

import com.badlogic.gdx.Gdx;
import com.cell.client.Main;

/**
 * Created by kongo on 17.03.16.
 */
public class ChatController {
    private final ChatWindow chatWindow;

    public ChatController(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    public void addMessage (final String message) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                chatWindow.addMessage(message);
            }
        });
    }

    public void sendMessage(){
        chatWindow.sendMessage();
    }
}
