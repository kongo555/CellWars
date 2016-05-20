package com.cell.client.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cell.client.GameClient;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by kongo on 31.03.16.
 */
public class UserWindow extends VisWindow {
    public UserWindow(final MenuScreen menuScreen, final GameClient gameClient) {
        super("User");

        setMovable(false);

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton startGameButton = new VisTextButton("Start game");
        VisTextButton editButton = new VisTextButton("Edit");


        VisTable buttonTable = new VisTable(true);
        buttonTable.add(startGameButton);
        buttonTable.add(editButton);

        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameClient.enterRoom();
                menuScreen.openWaitingWindow();
            }
        });

        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameClient.sendUserInfoRequest();
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
    }
}
