package com.cell.server.database;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by kongo on 05.05.16.
 */
public class DataBaseView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DataBase dataBase = new DataBase();
        TableView<Integer> table = null;
        try {
            table = createTable(dataBase.getBoughtItemsInCategory(1));
            table = createTable(dataBase.getBoughtItemsByDate("2016-05-01", "2016-05-30"));
//            table = createTable(dataBase.getAmountOfTransactions());
        } catch (DataBaseExepction dataBaseExepction) {
            dataBaseExepction.printStackTrace();
        }

        primaryStage.setScene(new Scene(new BorderPane(table), 600, 600));
        primaryStage.show();
    }

    private TableView<Integer> createTable(List<ColumnDescription> list) {
        TableView<Integer> table = new TableView<>();
        for (int i = 0; i < list.get(0).list.size(); i++) {
            table.getItems().add(i);
        }

        for (int i = 0; i < list.size(); i++) {
            addColumn(table, list.get(i));
        }

        return table;
    }

    private void addColumn(TableView<Integer> table, ColumnDescription columnDescription) {
        TableColumn column = null;
        if (columnDescription.list.get(0) instanceof String) {
            column = addStringColumn(columnDescription);
        } else if (columnDescription.list.get(0) instanceof Integer) {
            column = addIntegerColumn(columnDescription);
        }
        table.getColumns().add(column);
    }

    private TableColumn addStringColumn(ColumnDescription columnDescription) {
        TableColumn<Integer, String> column = new TableColumn<>(columnDescription.name);
        column.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper((String) columnDescription.list.get(rowIndex));
        });

        return column;
    }

    private TableColumn addIntegerColumn(ColumnDescription columnDescription) {
        TableColumn<Integer, Number> column = new TableColumn<>(columnDescription.name);
        column.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper((Integer) columnDescription.list.get(rowIndex));
        });

        return column;
    }
}