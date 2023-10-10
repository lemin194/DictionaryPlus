package dictionary.services;


import dictionary.models.Entity.Word;
import dictionary.models.Entity.WordCollectionManagement;

import java.util.List;

public class CollectionLookUpService {
    public List<String> findCollection(String prefix) {
        return WordCollectionManagement.collectionNamesContainPrefix(prefix);
    }
}
