package com.cell.client.menu;

import com.cell.client.GameClient;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;


/**
 * Created by kongo on 16.03.16.
 */
public class LoginForm extends VisWindow {
    private final MenuScreen menuScreen;

    public LoginForm (final MenuScreen menuScreen, final GameClient gameClient) {
        super("Login");
        this.menuScreen = menuScreen;

        setModal(true);
        closeOnEscape();
        addCloseButton();
        setMovable(false);

        TableUtils.setSpacingDefaults(this);
        defaults().padRight(1);
        defaults().padLeft(1);
        columnDefaults(0).left();

        VisTextButton acceptButton = new VisTextButton("accept");

        final VisValidatableTextField nameField = new VisValidatableTextField();
        final VisValidatableTextField passwordField = new VisValidatableTextField();
        passwordField.setPasswordMode(true);

        VisLabel errorLabel = new VisLabel();
        errorLabel.setColor(Color.RED);

        VisTable buttonTable = new VisTable(true);
        buttonTable.add(errorLabel).expand().fill();
        buttonTable.add(acceptButton);

        add(new VisLabel("Name: "));
        add(nameField).expand().fill();
        row();
        add(new VisLabel("Password: "));
        add(passwordField).expand().fill();
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
        validator.setSuccessMessage("all good!");
        validator.notEmpty(nameField, "Name cannot be empty");
        validator.notEmpty(passwordField, "Password cannot be empty");

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                gameClient.login(nameField.getText(), passwordField.getText());
                menuScreen.openWaitingWindow();
            }
        });

        pack();
        setSize(getWidth() + 60, getHeight());
        centerWindow();
    }

    @Override
    protected void close () {
        super.close();
        menuScreen.openMainMenu();
    }
}
