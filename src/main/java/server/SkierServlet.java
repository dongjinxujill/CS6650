package server;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();
    Map<String, String[]> body = request.getParameterMap();

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String jsonString = new Gson().toJson("missing paramterers");
      out.write(jsonString);
      return;
    }

    String[] urlParts = urlPath.split("/");

    if (isUrlValid(urlParts) && isBodyValid(body)) {
      response.setStatus(HttpServletResponse.SC_OK);
      String jsonString = new Gson().toJson("1");
      out.write(jsonString);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String jsonString = new Gson().toJson("invalid url");
      out.write(jsonString);
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String jsonString = new Gson().toJson("missing paramterers");
      out.write(jsonString);
      return;
    }

    String[] urlParts = urlPath.split("/");

    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String jsonString = new Gson().toJson("invalid url");
      out.write(jsonString);
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      String jsonString = new Gson().toJson("1");
      out.write(jsonString);
    }
  }

  private boolean isBodyValid(Map<String, String[]> body) {
    if (body.containsKey("time") && body.containsKey("liftID") && body.size() == 2) {
      return true;
    }
    return false;
  }
  private boolean isUrlValid(String[] urlPath) {

    if (urlPath.length == 8 && ServletHelper.isInteger(urlPath[1]) && urlPath[2].equals("seasons")
        && ServletHelper.isInteger(urlPath[3]) && urlPath[4].equals("days")
        && ServletHelper.isInteger(urlPath[5]) && urlPath[6].equals("skiers")
        && ServletHelper.isInteger(urlPath[7])) {
      return true;
    }
    return urlPath.length == 3 && ServletHelper.isInteger(urlPath[1])
        && urlPath[2].equals("vertical");
  }
}
