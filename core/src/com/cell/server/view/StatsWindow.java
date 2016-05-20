package com.cell.server.view;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.cell.server.database.DataBase;
import com.kotcrab.vis.ui.widget.*;

/**
 * Created by kongo on 14.04.16.
 */
public class StatsWindow extends VisWindow {
    private final ServerView serverView;
    
    
    //// TODO: 04.05.16 wszystko
    public StatsWindow(final ServerView serverView, DataBase dataBase) {
        super("Stats");
        this.serverView = serverView;

        setModal(true);
        closeOnEscape();
        addCloseButton();
        setMovable(false);

        //TableUtils.setSpacingDefaults(this);
        //defaults().padRight(1);
        //defaults().padLeft(1);
        //columnDefaults(0).left();


        VisTable table = getStatsTable(dataBase);
        //table.setFillParent(true);
        VisScrollPane scrollPane = new VisScrollPane(table);
        scrollPane.setFadeScrollBars(false);
        //scrollPane.setFillParent(true);
        HorizontalGroup description = new HorizontalGroup();
        description.addActor(new VisLabel("Name"));
        description.addActor(new VisLabel("Score"));
        description.space(20);
        description.padRight(scrollPane.getScrollBarWidth());
        description.padLeft(10);
        add(description).row();
        add(scrollPane).expand().fill();

        pack();
        //setFillParent(true);
    }

    private VisTable getStatsTable(DataBase dataBase){
        VisTable table = new VisTable();
        /*for (String text : dataBase.getStats()) {
            String word[] = text.split(" ");
            table.add(new VisLabel(word[0]));
            table.add(new VisLabel(word[1])).row();
        }*/
        return table;
    }

    @Override
    protected void close () {
        super.close();
        serverView.openOptionsWindow();
    }
}