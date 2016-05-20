package com.cell.client.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cell.network.Network;
import com.cell.client.GameClient;
import com.cell.network.user.UserInfo;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.*;

/**
 * Created by kongo on 31.03.16.
 */
public class EditForm extends VisWindow {
    private MenuScreen menuScreen;

    public EditForm (final MenuScreen menuScreen, final GameClient gameClient, UserInfo userInfo) {
        super("Edit");
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
        nameField.setText(userInfo.name);
        final VisValidatableTextField passwordField = new VisValidatableTextField();
        passwordField.setPasswordMode(true);
        final VisValidatableTextField emailField = new VisValidatableTextField();
        emailField.setText(userInfo.email);

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
        add(new VisLabel("Email: "));
        add(emailField).expand().fill();
        row();
        add(buttonTable).fill().expand().colspan(2).padBottom(3);

        SimpleFormValidator validator; //for GWT compatibility
        validator = new SimpleFormValidator(acceptButton, errorLabel, "smooth");
        validator.setSuccessMessage("all good!");
        validator.notEmpty(nameField, "Name cannot be empty");
        validator.notEmpty(passwordField, "Password cannot be empty");
        validator.notEmpty(emailField, "Email cannot be empty");

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                gameClient.sendUpadateUser(nameField.getText(), passwordField.getText(), emailField.getText());
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
        menuScreen.openUserWindow();
    }
}
