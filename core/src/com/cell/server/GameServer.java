package com.cell.server;

import com.cell.network.Network;
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
import com.cell.server.database.DataBase;
import com.cell.server.room.Room;
import com.cell.server.room.ServerCell;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kongo on 17.03.16.
 */
public class GameServer extends Listener {
    private final Server server;
    private final HashMap<String, UserSession> loggedIn = new HashMap<String, UserSession>();
    private final Main main;
    private final DataBase dataBase;
    private Room room;

    public GameServer(final Main main, final DataBase dataBase, final Room room) throws IOException {
        this.main = main;
        this.dataBase = dataBase;
        this.room = room;
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        Network.register(server);

        server.addListener(this);
        server.bind(Network.port);
        server.start();
    }

    private void loginSession(GameConnection gameConnection, String name, int id) {
        gameConnection.name = name;
        gameConnection.sendTCP(new LoginSuccessful());

        UserSession userSession = new UserSession();
        userSession.name = name;
        userSession.id = id;
        loggedIn.put(name, userSession);

        // Send a "connected" message to everyone except the new client.
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.text = name + " connected.";
        server.sendToAllExceptTCP(gameConnection.getID(), chatMessage);
    }

    public void received(Connection connection, Object object) {
        GameConnection gameConnection = (GameConnection) connection;

        if (object instanceof InputPackage) {
            if (gameConnection.name == null)
                return;

            InputPackage inputPackage = (InputPackage) object;
            if (validateInput(inputPackage)) {
                ServerCell entity = room.getEntities().get(connection.getID());
                entity.applyInput(inputPackage);
                if (entity.getX() > room.getWorldWidth())
                    entity.setX(room.getWorldWidth());
                else if (entity.getX() < 0)
                    entity.setX(0);

                if (entity.getY() > room.getWorldHeight())
                    entity.setY(room.getWorldHeight());
                else if (entity.getY() < 0)
                    entity.setY(0);


                entity.setLastProcessedInput(inputPackage.input_sequence_number);
                room.checkCollision(entity);
            }

            return;
        }

        if (object instanceof ChatMessage) {
            // Ignore the object if a client tries to chat before registering a name.
            if (gameConnection.name == null)
                return;
            ChatMessage chatMessage = (ChatMessage) object;
            // Ignore the object if the chat message is invalid.
            String message = chatMessage.text;
            if (message == null) return;
            message = message.trim();
            if (message.length() == 0) return;
            // Prepend the chatConnection's name and send to everyone.
            chatMessage.text = gameConnection.name + ": " + message;
            server.sendToAllTCP(chatMessage);
            return;
        }

        if (object instanceof AddPlayer) {
            if (gameConnection.name == null)
                return;
            if (room == null) {
                room = new Room(this);
                main.setRoom(room);
            }
            //todo random position and color
            ServerCell entity = new ServerCell(gameConnection, 50, 50, -1, Room.startCellSize, 0);
            room.getEntities().put(connection.getID(), entity);

            connection.sendTCP(new WorldInfo(room.getWorldWidth(), room.getWorldHeight()));

            return;
        }

        if (object instanceof RemovePlayer) {
            if (gameConnection.name == null)
                return;
            room.getEntities().remove(connection.getID());

            return;
        }

        if (object instanceof LoginRequest) {
            // Ignore the object if a client has already registered a name.
            if (gameConnection.name != null) return;
            // Ignore the object if the name is invalid.
            String name = ((LoginRequest) object).name;
            if (name == null) return;
            name = name.trim();
            if (name.length() == 0) return;

            if (checkIfLogged(name)) {
                connection.sendTCP(new LoginFailure());
                return;
            }

            // Ignore the object if the password is invalid.
            String password = ((LoginRequest) object).password;
            if (password == null) return;
            password = password.trim();
            if (password.length() == 0) return;

            // Search in database for name and password
            int id = dataBase.login(name, password);
            if (id == -1) {
                connection.sendTCP(new LoginFailure());
                return;
            }

            loginSession(gameConnection, name, id);

            return;
        }

        if (object instanceof RegisterRequest) {
            // Ignore the object if a client has already registered a name.
            if (gameConnection.name != null) return;
            // Ignore the object if the name is invalid.
            String name = ((RegisterRequest) object).name;
            if (name == null) return;
            name = name.trim();
            if (name.length() == 0) return;

            String password = ((RegisterRequest) object).password;
            if (password == null) return;
            password = password.trim();

            if (password.length() == 0) return;
            String email = ((RegisterRequest) object).email;
            if (email == null) return;
            email = email.trim();
            if (email.length() == 0) return;

            if (!dataBase.register(name, password, email)) {
                connection.sendTCP(new RegisterFailure());
                return;
            }
            connection.sendTCP(new RegisterSuccessful());

            return;
        }

        if (object instanceof UserInfoRequest) {
            if (gameConnection.name == null)
                return;
            UserInfo userInfo;
            userInfo = dataBase.getUserInfo(loggedIn.get(gameConnection.name).id);
            if (userInfo == null) {
                System.out.println("userinfo bug");
            }

            connection.sendTCP(userInfo);

            return;
        }

        if (object instanceof UserInfo) {
            if (gameConnection.name == null)
                return;
            UserInfo userInfo = (UserInfo) object;
            dataBase.updateUser(loggedIn.get(gameConnection.name).id, userInfo);

            connection.sendTCP(new EditUserSuccessful());

            return;
        }
    }

    public void disconnected(Connection c) {
        GameConnection connection = (GameConnection) c;
        if (connection.name != null) {
            loggedIn.remove(connection.name);

            // Announce to everyone that someone (with a registered name) has left.
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.text = connection.name + " disconnected.";
            server.sendToAllTCP(chatMessage);

            room.getEntities().remove(connection.getID());
        }
    }

    private boolean checkIfLogged(String name) {
        return loggedIn.containsKey(name);
    }

    public boolean validateInput(InputPackage input) {
        return Math.abs(input.pressTimeVertical) <= 0.03;
    }

    public void removePlayer(ServerCell entity) {
        //room.getEntities().remove(entity.getConnection().getID());
        entity.getConnection().sendTCP(new GameOver());
    }


    public void sendToAllEntitiesTCP(Object object) {
        for (Map.Entry<Integer, ServerCell> entry : room.getEntities().entrySet()) {
            entry.getValue().getConnection().sendTCP(object);
        }
    }

    public void dispose() {
        server.stop();
    }
}
