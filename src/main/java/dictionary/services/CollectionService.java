package dictionary.services;


import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.WordCollectionManagement;

import java.util.List;

public class CollectionService {
    public List<String> findCollection(String prefix) {
        return WordCollectionManagement.collectionNamesContainPrefix(prefix);
    }

    public void deleteCollection(String collectionName) {

    }

    public static void main(String[] args) {

    }
}
