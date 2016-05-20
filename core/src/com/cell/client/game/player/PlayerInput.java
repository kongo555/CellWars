package com.cell.client.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.cell.client.GameClient;
import com.cell.client.game.chat.ChatController;
import com.cell.network.game.InputPackage;

import java.util.LinkedList;

/**
 * Created by kongo on 02.04.16.
 */
public class PlayerInput extends InputAdapter {
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private int input_sequence_number;
    private LinkedList<InputPackage> pending_inputs;

    //// FIXME: 02.04.16
    private boolean client_side_prediction = true;
    private GameClient client;
    private ChatController chatController;
    private Cell cell;


    public PlayerInput(GameClient client, ChatController chatController, Cell cell) {
        this.client = client;
        this.chatController = chatController;
        this.cell = cell;
        pending_inputs = new LinkedList<InputPackage>();
    }

    public void processInputs(float delta) {
        InputPackage input;
        boolean send = false;
        float vertical = 0, horizontal = 0;
        if (right) {
            horizontal = delta;
            send = true;
        } else if (left) {
            horizontal = -delta;
            send = true;
        }

        if (up) {
            vertical = delta;
            send = true;
        } else if (down) {
            vertical = -delta;
            send = true;
        }

        if (!send)
            return;

        input = new InputPackage(horizontal, vertical);

        // Send the input to the server.
        input.input_sequence_number = this.input_sequence_number++;
        //input.entity_id = client.entity_id;

        client.sendInputPackage(input);

        // Do client-side prediction.
        if (client_side_prediction) {
            cell.applyInput(input);
        }

        // Save this input for later reconciliation.
        pending_inputs.add(input);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                left = true;
                break;
            case Input.Keys.RIGHT:
                right = true;
                break;
            case Input.Keys.UP:
                up = true;
                break;
            case Input.Keys.DOWN:
                down = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                left = false;
                break;
            case Input.Keys.RIGHT:
                right = false;
                break;
            case Input.Keys.UP:
                up = false;
                break;
            case Input.Keys.DOWN:
                down = false;
                break;
            case Input.Keys.ENTER:
                chatController.sendMessage();
                break;
        }
        return true;
    }

    public LinkedList<InputPackage> getPending_inputs() {
        return pending_inputs;
    }
}
