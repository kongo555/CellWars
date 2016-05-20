package com.cell.client.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;


/**
 * Created by kongo on 16.03.16.
 */
public class MenuWindow extends VisWindow {
    public MenuWindow(final MenuScreen menuScreen) {
        super("Menu");

        setMovable(false);

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton loginButton = new VisTextButton("Login");
        VisTextButton registerButton = new VisTextButton("Register");


        VisTable buttonTable = new VisTable(true);
        buttonTable.add(loginButton).expand().fill().row();
        buttonTable.add(registerButton).expand().fill();

        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.openLoginForm();
            }
        });

        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.openRegisterForm();
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
    }
}
