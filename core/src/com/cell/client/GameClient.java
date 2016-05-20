package com.cell.client;

import com.cell.client.game.player.Cell;
import com.cell.client.game.player.PlayerInput;
import com.cell.network.Network;
import com.cell.client.game.chat.ChatController;
import com.cell.network.chat.ChatMessage;
import com.cell.network.game.*;
import com.cell.network.login.LoginFailure;
import com.cell.network.login.LoginRequest;
import com.cell.network.login.LoginSuccessful;
import com.cell.network.register.RegisterFailure;
import com.cell.network.register.RegisterRequest;
import com.cell.network.register.RegisterSuccessful;
import com.cell.network.user.EditUserSuccessful;
import com.cell.network.user.UserInfo;
import com.cell.network.user.UserInfoRequest;
import com.cell.utils.HashGenerationException;
import com.cell.utils.SecurityUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * Created by kongo on 17.03.16.
 */
public class GameClient {
    private final Main main;
    private final ChatController chatController;
    private final Client client;

    private boolean server_reconciliation = true;

    public GameClient (final Main main, final Client client, final ChatController chatController) {
        this.main = main;
        this.client = client;
        this.chatController = chatController;

        this.client.start();
        Network.register(this.client);

        this.client.addListener(new Listener() {
            public void connected (Connection connection) {
            }

            public void received (Connection connection, Object object) {
                if(object instanceof WorldState){
                    main.getGameScreen().getEntities().clear();

                    proccesPlayerState(((WorldState)object).getPlayerState());

                    EntityState[] states = ((WorldState)object).getStates();
                    for (int i = 0; i < states.length; i++) {
                        EntityState state = states[i];
                        main.getGameScreen().getEntities().add(new Cell(state.x, state.y, state.size, state.color));
                    }
                }

                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage)object;
                    chatController.addMessage(chatMessage.text);
                    return;
                }

                if(object instanceof WorldInfo){
                    WorldInfo worldInfo = (WorldInfo)object;
                    main.setGameScreen(worldInfo.width, worldInfo.height);
                }

                if (object instanceof GameOver) {
                    main.setMenuScreen();
                    return;
                }

                if (object instanceof LoginSuccessful) {
                    main.getMenuScreen().openUserWindow();
                    return;
                }

                if (object instanceof LoginFailure) {
                    main.getMenuScreen().createDailog("Warning","Failed to login");
                    return;
                }

                if (object instanceof RegisterSuccessful) {
                    main.getMenuScreen().openMainMenu();
                    main.getMenuScreen().createDailog("Successful","Your account has been created");
                    return;
                }

                if (object instanceof RegisterFailure) {
                    main.getMenuScreen().createDailog("Warning","Failed to register");
                    return;
                }

                if (object instanceof UserInfo) {
                    main.getMenuScreen().openEditForm((UserInfo) object);
                    return;
                }

                if (object instanceof EditUserSuccessful) {
                    main.getMenuScreen().openUserWindow();
                    main.getMenuScreen().createDailog("Successful","Edit successful");
                    return;
                }
            }

            public void disconnected (Connection connection) {
                client.sendTCP(new RemovePlayer());
            }
        });

    }

    public void proccesPlayerState(PlayerState playerState){
        // Set the position sent by the server.
        Cell cell = main.getGameScreen().getCell();
        cell.setX(playerState.x);
        cell.setY(playerState.y);


        PlayerInput playerInput = main.getGameScreen().getPlayerInput();
        if (server_reconciliation) {
            // Server Reconciliation. Re-apply all the inputs not yet processed by
            // the server.
            int j = 0;
            while (j < playerInput.getPending_inputs().size()) {
                InputPackage input = playerInput.getPending_inputs().getFirst();
                if (input.input_sequence_number <= playerState.last_processed_input) {
                    // Already processed. Its effect is already taken into account
                    // into the world update we just got, so we can drop it.
                    playerInput.getPending_inputs().removeFirst();
                } else {
                    // Not processed by the server yet. Re-apply it.
                    cell.applyInput(input);
                    j++;
                }
            }
        } else {
            // Reconciliation is disabled, so drop all the saved inputs.
            playerInput.getPending_inputs().clear();
        }
    }

    public void login(final String name, final String password){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.name = name;
        //loginRequest.password = password;
        try {
            loginRequest.password = SecurityUtils.generateSHA256(password);
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        client.sendTCP(loginRequest);
    }

    public void register(final String name, final String password, final String email){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.name = name;
        try {
            registerRequest.password = SecurityUtils.generateSHA256(password);
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        registerRequest.email = email;

        client.sendTCP(registerRequest);
    }

    public void sendUserInfoRequest(){
        client.sendTCP(new UserInfoRequest());
    }

    public void sendUpadateUser(final String name, final String password, final String email){
        UserInfo userInfo = new UserInfo();
        userInfo.name = name;
        try {
            userInfo.password = SecurityUtils.generateSHA256(password);
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        userInfo.email = email;

        client.sendTCP(userInfo);
    }

    public void enterRoom(){
        client.sendTCP(new AddPlayer());
    }

    public void sendInputPackage(InputPackage inputPackage){
        client.sendTCP(inputPackage);
    }

    public void connect(){
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, "localhost", Network.port);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Problems with conecction");
                }
            }
        }.start();
    }

    public void dispose(){
        client.close();
    }

}
