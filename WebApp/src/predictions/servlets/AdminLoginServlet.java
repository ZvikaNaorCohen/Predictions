package predictions.servlets;

import engine.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import predictions.utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = "/adminLogin")
public class AdminLoginServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        synchronized (this){
            if(userManager.isAdminConnected()){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Admin already connected!");
            }
            else{
                userManager.adminNowConnected();
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Inside doGet!!");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
