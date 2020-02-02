package bananapp;

import com.google.cloud.automl.v1.AnnotationPayload;
import com.google.cloud.automl.v1.ExamplePayload;
import com.google.cloud.automl.v1.Image;
import com.google.cloud.automl.v1.ModelName;
import com.google.cloud.automl.v1.PredictRequest;
import com.google.cloud.automl.v1.PredictResponse;
import com.google.cloud.automl.v1.PredictionServiceClient;
import com.google.protobuf.ByteString;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

class VisionClassificationPredict {
  /*
  public static void main(String[] args) throws IOException {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "bananapp-264116";
    String modelId = "ICN1230199579853455360";
    String filePath = "/home/kuba/Downloads/b.jpg";
    predict(projectId, modelId, filePath);
  }
  */

  static Double[] predict(String projectId, String modelId, String filePath) throws IOException {
    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests. After completing all of your requests, call
    // the "close" method on the client to safely clean up any remaining background resources.
    Double[] prediction = new Double[2];
    try (PredictionServiceClient client = PredictionServiceClient.create()) {

      // Get the full path of the model.
      ModelName name = ModelName.of(projectId, "us-central1", modelId);
      ByteString content = ByteString.copyFrom(Files.readAllBytes(Paths.get(filePath)));
      Image image = Image.newBuilder().setImageBytes(content).build();
      ExamplePayload payload = ExamplePayload.newBuilder().setImage(image).build();
      PredictRequest predictRequest =
              PredictRequest.newBuilder()
                      .setName(name.toString())
                      .setPayload(payload)
                      .putParams(
                              "score_threshold", "0.0") // [0.0-1.0] Only produce results higher than this value
                      .build();
      System.out.println("gcp request:"+predictRequest);
      PredictResponse response = client.predict(predictRequest);
      System.out.println("gcp response:"+response);
      /*
      for (AnnotationPayload annotationPayload : response.getPayloadList()) {
        System.out.format("Predicted class name: %s\n", annotationPayload.getDisplayName());
        System.out.format(
                "Predicted class score: %.2f\n", annotationPayload.getClassification().getScore());
      }
       */
      System.out.println("that's the one: "+response.getPayload(0));

      prediction[0] = Double.parseDouble(response.getPayload(0).getDisplayName());
      Double prediction1 = (double) response.getPayload(0).getClassification().getScore();
      prediction[1] = round(prediction1, 2);
      for (AnnotationPayload onePayload : response.getPayloadList()) {
        if (onePayload.getClassification().getScore() > prediction[1]) {
          prediction[1] = round(onePayload.getClassification().getScore(), 2);
          prediction[0] = Double.parseDouble(onePayload.getDisplayName());
        }
      }
    }
    return prediction;
  }

  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}