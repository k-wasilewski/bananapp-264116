package bananapp;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.paging.Page;
import com.google.api.services.iam.v1.Iam;
import com.google.api.services.iam.v1.IamScopes;
import com.google.api.services.iam.v1.model.ListRolesResponse;
import com.google.api.services.iam.v1.model.Role;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import org.apache.log4j.BasicConfigurator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quickstart {

    public static void GetData() throws Exception {
        // You can specify a credential file by providing a path to GoogleCredentials.
        // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
        List results = new ArrayList();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/home/kuba/Desktop/bananas/settings/bananapp-264116-0483d7ffaad7.json"))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:---------------------------------------------------------------------------");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
        System.out.println("Credentials:--------------------------------------------------------------------");
        System.out.println(credentials);

        System.out.println("Models:---------------------------------------------------------------");
        ListModels.listModels();

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId("bananapp-266908")
                .setCredentials(GoogleCredentials.fromStream(new
                        FileInputStream("/home/kuba/Desktop/bananas/settings/bananapp-264116-0483d7ffaad7.json"))).build();

        //Storage storage = storageOptions.getService();
        // Get credentials
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault()
                        .createScoped(Collections.singleton(IamScopes.CLOUD_PLATFORM));
        System.out.println("------------CREDENTIAL-------------");
        System.out.println(credential);
        // Create the Cloud IAM service object
        Iam service =
                new Iam.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(),
                        credential)
                        .setApplicationName("quickstart")
                        .build();
        System.out.println("--------service----------");
        System.out.println(service);
        // Call the Cloud IAM Roles API
        ListRolesResponse response = service.roles().list().execute();
        List<Role> roles = response.getRoles();

        // Process the response
        System.out.println("Roles:----------------------------------------------------------");
        for (Role role : roles) {
            System.out.println("Title: " + role.getTitle());
            System.out.println("Name: " + role.getName());
            System.out.println("Description: " + role.getDescription());
            System.out.println();
        }



        System.out.println("Prediction:---------------------------------------------------------");
        //VisionClassificationPredict.main(new String[0]);
    }
}