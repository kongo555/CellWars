package com.cell.server.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by kongo on 14.04.16.
 */
public class OptionsWindow extends VisWindow {
    public OptionsWindow(final ServerView serverView) {
        super("Menu");

        setMovable(false);

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton statsButton = new VisTextButton("Stats");
        VisTextButton cosButton = new VisTextButton("-");


        VisTable buttonTable = new VisTable(true);
        buttonTable.add(statsButton).expand().fill().row();
        buttonTable.add(cosButton).expand().fill();

        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                serverView.openStatsWindow();
            }
        });

        cosButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //menuScreen.openRegisterForm();
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
    }
}
