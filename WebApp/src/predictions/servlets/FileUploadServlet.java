package predictions.servlets;

import file.read.XMLRead;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static predictions.constants.Constants.PRDWORLDS_LIST;
import static predictions.constants.Constants.WORLDS_NAMES_LIST;


@WebServlet(name = "FileUploadServlet", urlPatterns = "/fileUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String xmlPath = request.getParameter("filePath");
        List<String> worldsNames = (List<String>) getServletContext().getAttribute(WORLDS_NAMES_LIST);
        List<PRDWorld> PRDWorldsList = (List<PRDWorld>) getServletContext().getAttribute(PRDWORLDS_LIST);
        if(worldsNames == null){
            worldsNames = new ArrayList<>();
            getServletContext().setAttribute(WORLDS_NAMES_LIST, worldsNames);
        }
        if(PRDWorldsList == null){
            PRDWorldsList = new ArrayList<>();
            getServletContext().setAttribute(PRDWORLDS_LIST, PRDWorldsList);
        }
        try {
            PRDWorld world = XMLRead.getWorldFromScheme(xmlPath);
            PRDWorldValid worldValidator = new PRDWorldValid();
            if(world == null){
                response.getWriter().println("Error reading file. ");
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
            else if(worldsNames.contains(world.getName())){
                response.getWriter().println("Already loaded file with world name: " + world.getName());
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
            if(!worldValidator.isWorldValid(world)){
                response.getWriter().println(worldValidator.getErrorMessage());
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
            else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("World is valid");
                PRDWorldsList.add(world);
                getServletContext().setAttribute(PRDWORLDS_LIST, PRDWorldsList);
                worldsNames.add(world.getName());
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error occurred while uploading XML file.");
        }
    }
}
