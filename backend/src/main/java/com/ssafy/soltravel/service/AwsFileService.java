package com.ssafy.soltravel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.soltravel.exception.FileConvertException;
import com.ssafy.soltravel.util.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AwsFileService{

  private final AmazonS3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final static String TEST_DIR = "test/";

  public String savePhoto(MultipartFile file, Long userId) throws IOException {
    File uploadFile = convert(file).orElseThrow(
        () -> new FileConvertException("파일 변환 에러")
    );
    return null;
  }

  private Optional<File> convert(MultipartFile file) throws IOException {
    Path convertFilePath = Paths.get(System.getProperty("user.home"), TEST_DIR, file.getOriginalFilename());

    try {
      Files.write(convertFilePath, file.getBytes());
      return Optional.of(convertFilePath.toFile());

    } catch (IOException e) {
      e.printStackTrace();

    }
    return Optional.empty();
  }

  private String upload(File file,String dirName ,Long userId) throws IOException {
    String fileName = String.format("%s%d/%s%s", dirName, userId, UUID.randomUUID(), file.getName());
    String uploadUrl = putS3(file, fileName);
    removeNewFile(file);
    return uploadUrl;
  }

  private String putS3(File file, String fileName)  {
    s3Client.putObject(
        new PutObjectRequest(bucket, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead)
    );
    return s3Client.getUrl(bucket, fileName).toString();
  }

  private void removeNewFile(File file) {
    if(file.delete()) {
      LogUtil.info("File delete success");
      return;
    }
    LogUtil.error("File delete fail");
  }

  public void createDir(String bucketName, String folderName) {
    s3Client.putObject(
        bucketName,
        folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata()
    );
  }

}
