package com.cell.test;

import com.badlogic.gdx.math.MathUtils;
import com.cell.server.database.DataBase;
import com.cell.utils.HashGenerationException;
import com.cell.utils.SecurityUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kongo on 04.05.16.
 */
public class DataBasePopulating {
    public Connection connection = null;

    public DataBasePopulating(){
        String url = "jdbc:mysql://localhost:3306/cellwars";
        String user = "testuser";
        String password = "test623";

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void populateUsers(int n){
        NameGenerator nameGenerator = null;
        try {
            nameGenerator = new NameGenerator("names.txt");
            for (int i = 0; i < n; i++) {
                register(nameGenerator.compose(MathUtils.random(1)+2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getUsers();
    }

    public void register(String name){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql="INSERT INTO User (name, password, email, score) VALUES (?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            String password = null;
            try {
                password = SecurityUtils.generateSHA256(name);
            } catch (HashGenerationException e) {
                e.printStackTrace();
            }
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name + "@gmail.com");
            preparedStatement.setInt(4, MathUtils.random(1000));
            resultSet = preparedStatement.executeQuery();


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }

    public void getUsers() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            pst = connection.prepareStatement("SELECT * FROM User");
            rs = pst.executeQuery();

            while (rs.next()) {
                System.out.print(rs.getInt(1));
                System.out.print(": ");
                System.out.print(rs.getString(2) + " ");
                //System.out.print(rs.getString(3) + " ");
                System.out.print(rs.getString(4) + " ");
                System.out.println(rs.getInt(5) + " ");
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger("getUsers");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }

    public int getLastUserID(){
        PreparedStatement pst = null;
        ResultSet rs = null;

        int reulst = -1;

        try {

            pst = connection.prepareStatement("SELECT MAX(idPlayer) FROM User;");
            rs = pst.executeQuery();

            if (rs.next()) {
                reulst = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger("getUsers");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
        System.out.print(reulst);
        return reulst;
    }

    public void getItems(int category) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            if(category == 1)
                pst = connection.prepareStatement("SELECT name, scoreNeeded FROM Item where idCategory=?");
            else if(category == 2)
                pst = connection.prepareStatement("SELECT name, price FROM Item where idCategory=?");
            else
                return;

            pst.setInt(1, category);
            rs = pst.executeQuery();

            while (rs.next()) {
                //System.out.print(rs.getInt(1));
                //System.out.print(": ");
                System.out.print(rs.getString(1) + " ");
                System.out.println(rs.getInt(2));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger("getUsers");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }

    public void addItem(int idPlayer, int idItem){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql="INSERT INTO Transaction (idPlayer, idItem) VALUES (?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idPlayer);
            preparedStatement.setInt(2, idItem);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void giveFreeItems(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Integer> itemId = new ArrayList<Integer>();
        ArrayList<Integer> scoreNeeded = new ArrayList<Integer>();

        try {
            pst = connection.prepareStatement("SELECT idItem, scoreNeeded FROM Item where idCategory=?");
            pst.setInt(1, 1);
            rs = pst.executeQuery();

            while (rs.next()) {
                itemId.add(rs.getInt(1));
                scoreNeeded.add(rs.getInt(2));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger("getUsers");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }

        try {
            pst = connection.prepareStatement("SELECT idPlayer, score FROM User");
            rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                int score = rs.getInt(2);
                for (int i = 0; i < scoreNeeded.size(); i++) {
                    if(score > scoreNeeded.get(i))
                        addItem(id, itemId.get(i));
                }
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger("giveFreeItems");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        }
    }

    public static void main(String[] args){
        DataBasePopulating dataBasePopulating = new DataBasePopulating();
        ////    user    ////
        //dataBasePopulating.populateUsers(50);
        //dataBasePopulating.getUsers();
        //dataBasePopulating.getLastUserID();

        ////    items    ////
        //dataBasePopulating.getItems(1);
        //System.out.println();
        //dataBasePopulating.getItems(2);
        dataBasePopulating.giveFreeItems();



        if (dataBasePopulating.connection != null) {
            try {
                dataBasePopulating.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
