package server;

import java.util.Map;
import javax.servlet.ServletRequest;
import org.json.JSONObject;

public class ServletHelper {

  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch(NumberFormatException e) {
      return false;
    } catch(NullPointerException e) {
      return false;
    }
    return true;
  }
}
