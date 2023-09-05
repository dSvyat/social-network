package ua.vozniuk.socialnetwork.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class ImageService {
    private static final String BUCKET_NAME = "dvsctest1";
    private final static String ACCESS_KEY ="AKIAVPFOWW55SWI633W3";
    private final static String SECRET_KEY="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";
    public static void saveFile(MultipartFile multipartFile, String name){
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());
        BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_NORTH_1).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        PutObjectResult objectResult = null;
        try {
            objectResult = s3client.putObject(BUCKET_NAME, name, multipartFile.getInputStream(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static InputStream getImage(String objectName){
        BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_NORTH_1).withCredentials(new AWSStaticCredentialsProvider(creds)).build();
        S3Object object;
        try {
            object = s3client.getObject(new GetObjectRequest(BUCKET_NAME, objectName));
        } catch (AmazonS3Exception e){
            object = s3client.getObject(new GetObjectRequest(BUCKET_NAME, "emptyProfilePic"));
        }
        InputStream objectData = object.getObjectContent();
        return objectData;
    }
}
