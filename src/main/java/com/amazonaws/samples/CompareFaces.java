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
       Float similarityThreshold = 70F; //확률 임계값
       String sourceImage = "source.jpg";
       //버킷에 저장된 비교 이미지 - target1_correct.png / target2_incorrect.jpg / target3_1correct_3incorrect.jpg
       String targetImage = "target2_incorrect.jpg";
       String bucket = "hyemin-rekognition-bucket";
       
       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
       
       //S3 버킷에서 이미지 불러오도록 코드 수정
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

       //신뢰도 70 이상의 매치되는 얼굴이 있는 경우 이미지의 어느 좌표에 있는지 표시
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       for (CompareFacesMatch match: faceDetails){
         ComparedFace face= match.getFace();
         BoundingBox position = face.getBoundingBox();
         System.out.println("이미지의 (" + position.getLeft().toString()
               + ", " + position.getTop()
               + ") 위치에 있는 얼굴이 찾으려는 얼굴과 " + match.getSimilarity().toString()
               + "% 일치합니다.");

       }
       
       //매치되지 않는 얼굴의 개수 리턴
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
       System.out.println("찾으려는 얼굴과 일치하지 않는 얼굴이 " + uncompared.size()
            + "개 존재합니다.");
   }
}
