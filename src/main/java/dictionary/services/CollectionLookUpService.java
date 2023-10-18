package dictionary.services;


import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.WordCollectionManagement;

import java.util.List;

public class CollectionLookUpService {
    public static List<String> findCollection(String prefix) {
        return WordCollectionManagement.collectionNamesContainPrefix(prefix);
    }

    public static void deleteCollection(String collectionName) {
        if (!WordCollectionManagement.isExist(collectionName)) {
            return;
        }
        WordCollectionManagement.deleteCollection(collectionName);
        WordCollectionDao.deleteCollection(collectionName);
    }
}
