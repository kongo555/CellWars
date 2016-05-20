package com.cell.network.game;

/**
 * Created by kongo on 05.04.16.
 */
public class WorldState {
    private PlayerState playerState;
    private EntityState[] states;

    public WorldState() {
    }


    public WorldState(PlayerState playerState, EntityState[] states) {
        this.playerState = playerState;
        this.states = states;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public EntityState[] getStates() {
        return states;
    }
}
