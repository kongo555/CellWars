package com.cell.server.database;

import java.util.List;

/**
 * Created by kongo on 05.05.16.
 */
public class ColumnDescription {
    public String name;
    public List list;

    public ColumnDescription(String name, List list){
        this.name = name;
        this.list = list;
    }
}
