package bananapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "AuthImageController", value = "/auth/image")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
        maxFileSize = 10485760L, // 10 MB
        maxRequestSize = 20971520L // 20 MB
)
public class AuthImageController extends HttpServlet {
    String projectId = "bananapp-264116";
    String modelId = "ICN1230199579853455360";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        System.out.println("get received at auth backend8082");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String uname = request.getParameter("uname");

        System.out.println("post received at auth backend8082");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String APP_PATH = "/home/kuba/Desktop/CodersLab/bananapp-264116";
        String uploadFilePath = APP_PATH + File.separator + uname;
        System.out.println("upload file path: "+ uploadFilePath);

        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        //write all uploaded files
        PrintWriter writer = response.getWriter();

        for (Part part : request.getParts()) {
            if (part != null && part.getSize() > 0) {
                String fileName = part.getSubmittedFileName();
                System.out.println("file name: "+fileName);
                String contentType = part.getContentType();

                // allows only JPG files to be uploaded
                if (!contentType.equalsIgnoreCase("image/jpeg")) {
                    System.out.println("file type not supported");
                    continue;
                }
                System.out.println("content type: "+contentType);

                //part.write(uploadFilePath + File.separator + fileName);
                InputStream fileContent = part.getInputStream();
                String filePath = uploadFilePath + File.separator + fileName;
                File dir = new File(filePath);
                Files.copy(fileContent, dir.toPath());

                //Double[] prediction = VisionClassificationPredict.predict(projectId, modelId, filePath);
                Double[] prediction = {3.0, 0.77};
                writer.println("score:"+prediction[0]+",accuracy:"+prediction[1]);
                writer.close();
                writer.flush();

            }
        }
    }
}

