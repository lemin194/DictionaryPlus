package dictionary.models.Dao;

import dictionary.services.WordOperationService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public void storeCollection() {
        try {
            // storing wordCollection for next use
            String filePATH = "src/main/resources/data/Collection.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePATH))) {
                for (String collectionName : WordCollectionDao.queryCollectionName()) {
                    writer.write(collectionName + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeLastFind() {
        WordOperationService.close();
    }

}
