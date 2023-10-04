package dictionary.models.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordCollection {
    public static List<String> collectionNameList = new ArrayList<>();
    private String collectionName;
    public WordCollection(String collectionName) {
        this.collectionName = collectionName;
        collectionNameList.add(collectionName);
        Collections.sort(collectionNameList);
    }

    public static boolean isExist(String collectionName) {
        for (int i = 0; i < collectionNameList.size(); i++) {
            if (collectionNameList.get(i).equals(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validPrefix(String collectionName, String prefix) {
        for (int i = 0; i < prefix.length(); i++) {
            if (collectionName.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static List<String> collectionNamesContainPrefix(String prefix) {
        List<String> containPrefixList = new ArrayList<>();
        for (String collectionName : collectionNameList) {
            if (validPrefix(collectionName, prefix)) {
                containPrefixList.add(collectionName);
            }
        }
        return containPrefixList;
    }

}

