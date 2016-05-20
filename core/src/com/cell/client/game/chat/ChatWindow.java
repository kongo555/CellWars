package com.cell.client.game.chat;

import com.cell.network.chat.ChatMessage;
import com.esotericsoftware.kryonet.Client;
import com.kotcrab.vis.ui.widget.*;

/**
 * Created by kongo on 17.03.16.
 */
public class ChatWindow extends VisWindow {
    private final VisTable messageTable;
    private final VisScrollPane messageScrollPane;
    private final VisTextField messageTextField;
    private Client client;

    public ChatWindow(final Client client) {
        super("Chat");
        this.client = client;

        messageTable = new VisTable();
        messageScrollPane = new VisScrollPane(messageTable);

        //SimpleFormValidator messageTestField = new SimpleFormValidator(null);
        messageTextField = new VisTextField();

        VisTable table = new VisTable();
        table.add(messageScrollPane).minHeight(100).minWidth(200);
        table.row();
        table.add(messageTextField).expandX().fillX();

        add(table).expand().fill();

        pack();
        bottom().left();
    }

    public void addMessage(final String message) {
        messageTable.add(new VisLabel(message));
        messageTable.row();
        layout();
        messageScrollPane.setScrollY(messageScrollPane.getMaxY());
    }

    public void sendMessage() {
        messageTextField.focusField();
        String message = messageTextField.getText();
        messageTextField.setText("");
        message = message.trim();
        if (message.length() == 0)
            return;

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.text = message;
        messageTextField.setText("");
        client.sendTCP(chatMessage);
    }

    public void resize(int width, int height) {
        setPosition(0, 0);
    }
}

