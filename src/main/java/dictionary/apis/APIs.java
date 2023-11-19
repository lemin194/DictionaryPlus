package dictionary.apis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public interface APIs {

  HashMap<String, Object> request(String url);
  HashMap<String, Object> requestJsonBody(String requestUrl, String requestMethod, HashMap<String, String> header, HashMap<String, String> body);
  HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body)
      throws IOException;
  HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body, File file)
      throws IOException;

  HashMap<String, Object> requestBinaryBody(String requestUrl, HashMap<String, String> header, File file)
      throws IOException;


}
