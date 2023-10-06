package dictionary.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JsonHandler {
    public static String Handle(String json) {
        String regex = "\"extract\":\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }  else {
            return "Not found";
        }
    }
}

class DataFetch {
    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public String modifySpace(String param) {
        for (int i = 0; i < param.length(); i++) {
            if (param.charAt(i) == ' ') {
                param = param.substring(0,i) + "%20" + param.substring(i+1);
            }
        }
        return param;
    }
    public String getJSON(String param) {
        param = modifySpace(param);
        String link = "https://en.wikipedia.org/w/api.php"
                + "?action=query"
                + "&titles=" + param
                + "&redirects=true"
                + "&prop=extracts"
                + "&exintro"
                + "&explaintext"
                + "&format=json";
        System.out.println(link);
        String res = executePost(link, "");
        return res;
    }

}
public class WikFetchService {
    private DataFetch dataFetch = new DataFetch();
    public String search(String param) {
        String json_raw = dataFetch.getJSON(param);
        return JsonHandler.Handle(json_raw);
    }

    //----------------------------------------------------------------------------------
    public static void main(String[] args) throws IOException, InterruptedException {
        WikFetchService wfService = new WikFetchService();
        System.out.println(wfService.search("VietNam"));
    }
}
