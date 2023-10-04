package dictionary.models.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseClose {

    /**
     * Close connection, prepared statement and result set.
     * The closing order is: result set -> prepared statement -> connection.
     */
    public static void databaseClose(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        // close order is reverse open order
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
                System.out.println("Close success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}