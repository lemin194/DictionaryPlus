package dictionary.models.Entity;

import java.util.ArrayList;
import java.util.List;

public class WordCollectionManagement {
  public static List<String> collectionNameList = new ArrayList<>();
  public static List<WordCollection> allCollection = new ArrayList<>();

  static
  {

  }
  public static void addCollection(String collectionName) {
    if (!isExist(collectionName)) {
      collectionNameList.add(collectionName);
      allCollection.add(new WordCollection(collectionName));
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