package com.cell.network;

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
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        //login
        kryo.register(LoginRequest.class);
        kryo.register(LoginFailure.class);
        kryo.register(LoginSuccessful.class);
        //register
        kryo.register(RegisterRequest.class);
        kryo.register(RegisterSuccessful.class);
        kryo.register(RegisterFailure.class);
        //user
        kryo.register(UserInfoRequest.class);
        kryo.register(UserInfo.class);
        kryo.register(EditUserSuccessful.class);
        //chat
        kryo.register(ChatMessage.class);
        //game
        kryo.register(AddPlayer.class);
        kryo.register(RemovePlayer.class);
        kryo.register(InputPackage.class);
        kryo.register(WorldInfo.class);
        kryo.register(WorldState.class);
        kryo.register(PlayerState.class);
        kryo.register(EntityState.class);
        kryo.register(EntityState[].class);
        kryo.register(GameOver.class);
    }

}
