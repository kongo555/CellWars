package com.cell.network.game;

/**
 * Created by kongo on 02.04.16.
 */
public class InputPackage {
    public float pressTimeHorizontal;
    public float pressTimeVertical;
    public int input_sequence_number;

    public InputPackage() {
    }

    public InputPackage(float pressTimeHorizontal, float pressTimeVertical) {
        this.pressTimeHorizontal = pressTimeHorizontal;
        this.pressTimeVertical = pressTimeVertical;
    }
}
