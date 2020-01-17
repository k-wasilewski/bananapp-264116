package bananapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "ImageController", value = "/image")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
        maxFileSize = 10485760L, // 10 MB
        maxRequestSize = 20971520L // 20 MB
)
public class ImageController extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("post received at backend8082");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + "uploads";

        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        //write all uploaded files
        PrintWriter writer = response.getWriter();

        for (Part part : request.getParts()) {
            if (part != null && part.getSize() > 0) {
                String fileName = part.getSubmittedFileName();
                String contentType = part.getContentType();

                // allows only JPEG files to be uploaded
                if (!contentType.equalsIgnoreCase("image/jpg")) {
                    continue;
                }

                part.write(uploadFilePath + File.separator + fileName);

                writer.append("File successfully uploaded to "
                        + uploadFolder.getAbsolutePath()
                        + File.separator
                        + fileName
                        + "<br>\r\n");
            }
        }

    }
}
