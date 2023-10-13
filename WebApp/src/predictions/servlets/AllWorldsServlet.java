package predictions.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import generated.PRDWorld;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static predictions.constants.Constants.PRDWORLDS_LIST;

@WebServlet(name = "AllWorldsServlet", urlPatterns = "/getAllWorlds")
public class AllWorldsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<PRDWorld> prdWorldsList = (List<PRDWorld>) getServletContext().getAttribute(PRDWORLDS_LIST);
        if(prdWorldsList == null){
            prdWorldsList = new ArrayList<>();
        }

        String jsonWorlds = convertListToJson(prdWorldsList);
        response.setContentType("application/json");
        response.getWriter().println(jsonWorlds);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String convertListToJson(List<PRDWorld> prdWorldsList) {
         Gson gson = new Gson();
         return gson.toJson(prdWorldsList);
    }
}
