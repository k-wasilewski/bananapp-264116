package bananapp.controllers;

import bananapp.POJOs.Prediction;
import com.google.gson.Gson;

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

@WebServlet(name = "ImageController", value = "/image")
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
        maxFileSize = 10485760L, // 10 MB
        maxRequestSize = 20971520L // 20 MB
)
public class ImageController extends HttpServlet {
    String projectId = "bananapp-266908";
    String modelId = "ICN595543876115103744";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String uploadFilePath = createAndGetDir();

        //write all uploaded files
        PrintWriter writer = response.getWriter();

        try {
            copyPartsToDir(request, writer, uploadFilePath);
        } catch (Exception e) {
            writer.println(0);
            writer.close();
            writer.flush();
        }
    }

    private String createAndGetDir() {
        String systemuser = System.getProperty("user.name");

        String APP_PATH = "/home/"+systemuser+"/bananapp";
        String uploadFilePath = APP_PATH + File.separator + "instants";

        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        return uploadFilePath;
    }

    private void copyPartsToDir(HttpServletRequest request, PrintWriter writer,
                                String uploadFilePath) throws IOException, ServletException {
        for (Part part : request.getParts()) {
            if (part != null && part.getSize() > 0) {
                String fileName = part.getSubmittedFileName();
                String contentType = part.getContentType();

                // allows only JPG files to be uploaded
                if (!contentType.equalsIgnoreCase("image/jpeg")) {
                    writer.println("fail");
                    writer.close();
                    writer.flush();
                    continue;
                }

                InputStream fileContent = part.getInputStream();
                String filePath = uploadFilePath + File.separator + fileName;
                File dir = new File(filePath);

                while (dir.exists()) {
                    fileName = fileName.replaceFirst("[.][^.]+$", "")+"1.jpg";
                    filePath = uploadFilePath + File.separator + fileName;
                    dir = new File(filePath);
                }

                Files.copy(fileContent, dir.toPath());

                writePrediction(projectId, modelId, fileName, writer);
            }
        }
    }

    private void writePrediction(String projectId, String modelId,
                                 String fileName, PrintWriter writer) {
        //Double[] prediction = VisionClassificationPredict.predict(projectId, modelId, filePath);
        Double[] predictionValues = {3.0, 0.77};    //mockup
        //writer.println("score:" + prediction[0] + ",accuracy:" + prediction[1]);
        Prediction prediction = new Prediction(predictionValues[0],
                predictionValues[1]);
        String json = new Gson().toJson(prediction);
        writer.write(json);
        writer.close();
        writer.flush();
    }
}
