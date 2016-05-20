package com.cell.server.database;

import com.cell.network.user.UserInfo;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kongo on 16.03.16.
 */
public class DataBase {
    Connection connection = null;

    public DataBase() {
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

    public int login(String name, String password){
        int result = -1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql="SELECT idPlayer, Name, Password FROM User where name=? and password=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                System.out.println("found " + resultSet.getString(1));
                result = resultSet.getInt("idPlayer");
            }
            else{
                System.out.println("nope");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public boolean register(String name, String password, String email){
        boolean result = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql="INSERT INTO User (name, password, email) VALUES (?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            resultSet = preparedStatement.executeQuery();

            result = true;

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public UserInfo getUserInfo(int id){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UserInfo userInfo = null;

        try {
            String sql="SELECT name, email FROM User where idPlayer=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                //found
                userInfo = new UserInfo();
                userInfo.name = resultSet.getString(1);
                userInfo.email = resultSet.getString(2);
                System.out.println("found " + resultSet.getString(1));
            }
            else{
                //not found
                System.out.println("nope");
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return userInfo;
    }

    public boolean updateUser(int id, UserInfo userInfo){
        boolean result = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql="UPDATE User SET name = ?, password = ?, email = ?  WHERE idPlayer = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userInfo.name);
            preparedStatement.setString(2, userInfo.password);
            preparedStatement.setString(3, userInfo.email);
            preparedStatement.setInt(4, id);
            resultSet = preparedStatement.executeQuery();
            //todo zmien ustawianie result
            result = true;

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        return result;
    }

    public ArrayList<ColumnDescription> getStats(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<String> name = new ArrayList<String>();
        ArrayList<Integer> score = new ArrayList<Integer>();
        ArrayList<ColumnDescription> stats = new ArrayList<ColumnDescription>();
        try {
            String sql="SELECT name, score FROM User";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {

                name.add(resultSet.getString(1));
                score.add(resultSet.getInt(2));
            }
            stats.add(new ColumnDescription("Name", name));
            stats.add(new ColumnDescription("Score", score));

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return stats;
    }

    public ArrayList<ColumnDescription> getBoughtItemsInCategory(int idCategory) throws DataBaseExepction {
        if(idCategory>2 || idCategory<1)
            throw new DataBaseExepction();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<String> userName = new ArrayList<String>();
        ArrayList<String> itemName = new ArrayList<String>();
        ArrayList<ColumnDescription> transcation = new ArrayList<ColumnDescription>();
        try {
            String sql="SELECT User.name, Item.Name\n" +
                    "FROM User\n" +
                    "    JOIN Transaction ON (User.idPlayer = Transaction.idPlayer)\n" +
                    "    JOIN Item ON (Transaction.idItem = Item.idItem)\n" +
                    "WHERE\n" +
                    "   Item.idCategory = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCategory);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {

                userName.add(resultSet.getString(1));
                itemName.add(resultSet.getString(2));
            }
            transcation.add(new ColumnDescription("User Name", userName));
            transcation.add(new ColumnDescription("Item Name", itemName));

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return transcation;
    }

    public ArrayList<ColumnDescription> getBoughtItemsByDate(String start, String end) throws DataBaseExepction {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> userName = new ArrayList<String>();
        ArrayList<String> itemName = new ArrayList<String>();
        ArrayList<String> timestamp = new ArrayList<String>();
        ArrayList<ColumnDescription> transcation = new ArrayList<ColumnDescription>();
        try {
            String sql="SELECT User.name, Item.Name, Transaction.Data\n" +
                    "FROM User\n" +
                    "   JOIN Transaction ON (User.idPlayer = Transaction.idPlayer)\n" +
                    "   JOIN Item ON (Transaction.idItem = Item.idItem)\n" +
                    "WHERE\n" +
                    "   DATE_FORMAT(Data, '%Y-%m-%d') >= ? AND\n" +
                    "   DATE_FORMAT(Data, '%Y-%m-%d') <= ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, start);
            preparedStatement.setString(2, end);

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {

                userName.add(resultSet.getString(1));
                itemName.add(resultSet.getString(2));
                timestamp.add(dateFormat.format(resultSet.getTimestamp(3)));
            }
            transcation.add(new ColumnDescription("User Name", userName));
            transcation.add(new ColumnDescription("Item Name", itemName));
            transcation.add(new ColumnDescription("Date", timestamp));

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return transcation;
    }

    public ArrayList<ColumnDescription> getAmountOfTransactions() throws DataBaseExepction{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<String> name = new ArrayList<String>();
        ArrayList<Integer> amount = new ArrayList<Integer>();
        ArrayList<ColumnDescription> amountOfTransactions = new ArrayList<ColumnDescription>();
        try {
            String sql="SELECT Item.name, count(Transaction.idItem) as amount\n" +
                    "FROM Transaction \n" +
                    "\tJOIN Item ON (Transaction.idItem = Item.idItem)\n" +
                    "GROUP BY Transaction.idItem;";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                name.add(resultSet.getString(1));
                amount.add(resultSet.getInt(2));
            }
            amountOfTransactions.add(new ColumnDescription("Name", name));
            amountOfTransactions.add(new ColumnDescription("Score", amount));

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataBase.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }

        return amountOfTransactions;
    }

    public void dispose(){
        try {
            if (connection != null) {
                    connection.close();
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataBase.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
