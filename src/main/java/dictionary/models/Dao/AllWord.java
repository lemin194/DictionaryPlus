package dictionary.models.Dao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

class IndexWord {
    private int index;
    private String word;


    public IndexWord(int index, String word) {
        this.index = index;
        this.word = word;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return index + " " + word;
    }
}
public class AllWord {
    private static ArrayList<IndexWord> words = new ArrayList<>();
    private static int maxIndex = 0;
    static {
        Connection conn = DatabaseConnection.getConnection();
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM anhviet";
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                words.add(
                        new IndexWord(id, resultSet.getString(2))
                );
                maxIndex = Math.max(maxIndex, id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, null, resultSet);
        }
        words.sort((IndexWord w1, IndexWord w2) -> w1.getWord().compareToIgnoreCase(w2.getWord()));
//        for (IndexWord word : words) System.out.println(word.getWord());
    }

    public static ArrayList<IndexWord> getWords() {
        return words;
    }
    
    /**
     * Check if a string contain prefix or not.
     */
    private static boolean valid(String str, String prefix) {
        for (int i = 0; i < prefix.length(); i++) {
            if (str.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Find word has left most index that contain prefix str.
     */
    public static int leftMostIndex(String str) {
        int low = 0;
        int high = words.size() - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (words.get(mid).getWord().compareToIgnoreCase(str) >= 0) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        if (words.get(low).getWord().compareToIgnoreCase(str) < 0
                || !valid(words.get(low).getWord(), str)) {
            low = -1;
        }
        return low;
    }

    /**
     * Find word has right most index that contain prefix str.
     */
    private static int rightMostIndex(String str) {
        int low = leftMostIndex(str);
        if (low == -1) {
            return -1;
        }
        int high = words.size() - 1;
        while (low < high) {
            int mid = low + (high - low + 1) / 2;
            if (valid(words.get(mid).getWord(), str)) low = mid;
            else high = mid - 1;
        }
        return low;
    }

    /**
     * Find couple leftmost index and rightmost index.
     */
    public static ArrayList<Integer> wordsContainPrefix(String pref) {
        int left = leftMostIndex(pref);
        int right = rightMostIndex(pref);
        ArrayList<Integer> res = new ArrayList<>();
        res.add(left);
        res.add(right);
        return res;
    }

    public static void addWord(String word) {
        maxIndex++;
        words.add(new IndexWord(maxIndex, word));
        words.sort((IndexWord w1, IndexWord w2) -> w1.getWord().compareToIgnoreCase(w2.getWord()));
    }

    public static void deleteWord(int index) {
        words.remove(index);
    }

    public static int tableID(int index) {
        return words.get(index).getIndex();
    }

    public static int amountWord() {
        return words.size();
    }

    public static void main(String[] args) {
        words.forEach(System.out::println);
    }

}
