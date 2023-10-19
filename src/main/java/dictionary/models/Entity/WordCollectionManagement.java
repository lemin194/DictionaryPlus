package dictionary.models.Entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordCollectionManagement {

    public static List<String> collectionNameList = new ArrayList<>();
    public static List<WordCollection> allCollection = new ArrayList<>();

    static
    {
        String filePATH = "src/main/resources/data/Collection.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                collectionNameList.add(line);
                System.out.println("this come from Collection.txt" + line);
                System.out.println("is it two");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addCollection(String collectionName) {
        if (!isExist(collectionName)) {
            collectionNameList.add(collectionName);
            allCollection.add(new WordCollection(collectionName));
        }
        try {
            String filePATH = "src/main/resources/data/Collection.txt";
            FileWriter fileWriter = new FileWriter(filePATH);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(collectionName);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    public static void deleteCollection(String collectionName) {
        if (isExist(collectionName)) {
            int index = findCollectionByName(collectionName);
            collectionNameList.remove(index);
            allCollection.remove(index);
        }
    }

    public static boolean isExist(String collectionName) {
        for (String s : collectionNameList) {
            if (s.equals(collectionName)) {
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

    public static int findCollectionByName(String name) {
        for (int i = 0; i < collectionNameList.size(); i++) {
            if (collectionNameList.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
