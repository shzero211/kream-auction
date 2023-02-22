package site.shkrr.kreamAuction.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.shkrr.kreamAuction.exception.brand.UpLoadBrandImgFailException;
import site.shkrr.kreamAuction.service.storage.common.ImageType;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadImg(MultipartFile file, ImageType imageType){
        String fileOriName=file.getOriginalFilename();
        String uploadName=makeUploadName(fileOriName, imageType.getDirPath());

        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(bucket,uploadName,file.getInputStream(),metadata));
        } catch (IOException e) {
            throw new UpLoadBrandImgFailException("브랜드 이미지 업로드에 실패 하였습니다.");
        }
        return amazonS3.getUrl(bucket,uploadName).toString();
    }

    public String makeUploadName(String fileOriName,String dirPath){
        StringBuilder sb=new StringBuilder();
        UUID uuid=UUID.randomUUID();
        String extension=getExtension(fileOriName);
        sb.append(dirPath).append("/").append(uuid).append(".").append(extension);
        return sb.toString();
    }

    private String getExtension(String fileOriName) {

        int idx= fileOriName.lastIndexOf(".");

        return fileOriName.substring(idx+1);
    }
}
