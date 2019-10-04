package server;

import com.google.gson.Gson;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.ResortsListResorts;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "ServiceServlet")
public class ServiceServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();
    Map<String, String[]> body = request.getParameterMap();

    // check we have a URL!`
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
      String jsonString = new Gson().toJson("invalid");
      out.write(jsonString);
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

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

  private boolean isUrlValid(String[] urlPath) {
    if (urlPath.length == 0) return true;
    else if (urlPath.length == 3 && ServletHelper.isInteger(urlPath[1])
        && urlPath[2].equals("seasons")) {
      return true;
    }
    return false;
  }
  private boolean isBodyValid(Map<String, String[]> body) {
    if (body.containsKey("year") && body.size() == 1) {
      return true;
    }
    return false;
  }

}
