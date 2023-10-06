package predictions.servlets;

import com.google.gson.Gson;
import engine.FileUploader;
import engine.users.UserManager;
import file.read.XMLRead;
import file.validate.impl.PRDWorldValid;
import generated.PRDWorld;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import predictions.utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collection;
import java.util.Scanner;


@WebServlet(name = "FileUploadServlet", urlPatterns = "/fileUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String xmlPath = request.getParameter("filePath");
        try {
            PRDWorld world = XMLRead.getWorldFromScheme(xmlPath);
            PRDWorldValid worldValidator = new PRDWorldValid();
            if(!worldValidator.isWorldValid(world)){
                response.getWriter().println(worldValidator.getErrorMessage());
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
            else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("World is valid");
            }


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error occurred while uploading XML file.");
        }
    }
}
