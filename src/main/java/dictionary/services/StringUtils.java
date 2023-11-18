package dictionary.services;

import dictionary.models.Entity.Word;
import java.util.List;

public class StringUtils {
    public static String getFirstMeaning(Word w) {
        String meaning = w.getMeaning();
        List<String> meanings = List.of(meaning.split("\n"));
        for (String m : meanings) {
            m = m.trim().replace("- ", "");
            if (m.isEmpty()) {
                continue;
            }
            if (m.startsWith("(viết tắt)")) continue;
//      System.out.println(m);
            return m;
        }
        return meanings.get(0).trim().replace("- ", "");
    }
}