package com.cell.client.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by kongo on 07.04.16.
 */
public class WaitingWindow extends VisWindow {
    private static final String waitingText = "Waiting";
    private final MenuScreen menuScreen;
    Label waitingLabel;
    private float delay = 0.4f;
    private float time = 0.0f;
    private float maxTime = 10.0f;
    private float roundTime = 0.0f;

    public WaitingWindow(MenuScreen menuScreen) {
        super(waitingText);
        this.menuScreen = menuScreen;

        setModal(true);
        setMovable(false);

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        waitingLabel = getTitleLabel();

        pack();
        setSize(getWidth() + 60, getHeight());
    }

    public void update(float delta) {
        roundTime += delta;
        if (roundTime >= delay) {
            time += roundTime;
            if (time > maxTime) {
                close();
                menuScreen.createDailog("Warning", "Conecction problems");
            }

            roundTime = 0;
            waitingLabel.setText(waitingLabel.getText() + ".");
            if (waitingLabel.getText().length() > 10)
                waitingLabel.setText(waitingText);
        }
    }

    @Override
    protected void close() {
        super.close();
        menuScreen.openCurrentMainWindow();
    }
}
