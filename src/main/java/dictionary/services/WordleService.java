package dictionary.services;

import dictionary.models.Dao.AllWord;
import dictionary.models.Dao.DatabaseClose;
import dictionary.models.Dao.DatabaseConnection;
import dictionary.models.Entity.Word;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class WordleService {

    private static ArrayList<String> words;
    private static int maxIndex = 0;
    static {
        words = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM anhviet";
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                int idx = resultSet.getInt(1);
                String word = resultSet.getString(2);
                if (word.length() != 5) continue;
                words.add(word);
                maxIndex = Math.max(maxIndex, idx);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, null, resultSet);
        }
        words.sort((String w1, String w2) -> w1.compareToIgnoreCase(w2));
    }

    public static String genWords(){
        int n = words.size();
        Random rand = new Random();
        int randIndex = rand.nextInt(n);
        return words.get(randIndex);
    }

    public static boolean validGuess(String guess){
        int low = 0, high = words.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = guess.compareTo(words.get(mid));
            if (comparison == 0) return true;
            if (comparison > 0) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }
}
