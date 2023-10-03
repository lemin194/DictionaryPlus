package dictionary.models.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

public class AllWord {
    private static ArrayList<String> word;

    static {
        word = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM anhviet";
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                word.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, null, resultSet);
        }
    }
    private static boolean valid(String str, String prefix) {
        for (int i = 0; i < prefix.length(); i++) {
            if (str.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static int leftMostIndex(String str) {
        int low = 0;
        int high = word.size() - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (word.get(mid).compareToIgnoreCase(str) >= 0) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        if (word.get(low).compareToIgnoreCase(str) < 0 || !valid(word.get(low), str)) {
            low = -1;
        }
        return low;
    }

    private static int rightMostIndex(String str) {
        int low = leftMostIndex(str);
        if (low == -1) {
            return -1;
        }
        int high = word.size() - 1;
        while (low < high) {
            int mid = low + (high - low + 1) / 2;
            if (valid(word.get(mid), str)) low = mid;
            else high = mid - 1;
        }
        return low;
    }

    public static ArrayList<Integer> wordsContainPrefix(String pref) {
        int left = leftMostIndex(pref);
        int right = rightMostIndex(pref);
        ArrayList<Integer> res = new ArrayList<>();
        res.add(left);
        res.add(right);
        return res;
    }

}
