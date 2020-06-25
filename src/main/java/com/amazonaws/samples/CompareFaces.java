package com.amazonaws.samples;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import java.util.List;

public class CompareFaces {

   public static void main(String[] args) throws Exception{
       Float similarityThreshold = 70F; //Ȯ�� �Ӱ谪
       String sourceImage = "source.jpg";
       //��Ŷ�� ����� �� �̹��� - target1_correct.png / target2_incorrect.jpg / target3_1correct_3incorrect.jpg
       String targetImage = "target2_incorrect.jpg";
       String bucket = "hyemin-rekognition-bucket";
       
       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
       
       //S3 ��Ŷ���� �̹��� �ҷ������� �ڵ� ����
       CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(new Image()
            		   .withS3Object(new S3Object()
            				   .withName(sourceImage)
            				   .withBucket(bucket)))
               .withTargetImage(new Image()
            		   .withS3Object(new S3Object()
            				   .withName(targetImage)
            				   .withBucket(bucket)))
               .withSimilarityThreshold(similarityThreshold);

       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);

       //�ŷڵ� 70 �̻��� ��ġ�Ǵ� ���� �ִ� ��� �̹����� ��� ��ǥ�� �ִ��� ǥ��
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       for (CompareFacesMatch match: faceDetails){
         ComparedFace face= match.getFace();
         BoundingBox position = face.getBoundingBox();
         System.out.println("�̹����� (" + position.getLeft().toString()
               + ", " + position.getTop()
               + ") ��ġ�� �ִ� ���� ã������ �󱼰� " + match.getSimilarity().toString()
               + "% ��ġ�մϴ�.");

       }
       
       //��ġ���� �ʴ� ���� ���� ����
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
       System.out.println("ã������ �󱼰� ��ġ���� �ʴ� ���� " + uncompared.size()
            + "�� �����մϴ�.");
   }
}
