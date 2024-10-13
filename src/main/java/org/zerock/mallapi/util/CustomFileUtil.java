package org.zerock.mallapi.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {


    //이 클래스의 역할 : 1. 파일 업로드
    //                2. 파일 다운로드
    //인터페이스로 만들었으면 좀 더 확장성이 좋을거 같다.

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        if(tempFolder.exists() == false){
            tempFolder.mkdirs();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("-------------------------------");
        log.info(uploadPath);
    }

    // 이 method의 목적은?
    // 파일을 절대 경로에 업로드 하고
    // DB에 String으로 저장합니다.
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {

        if(files == null || files.size() == 0) {
            return List.of();
            //return null
        }

        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file : files) {

            String savedName = UUID.randomUUID().toString()+ "_" + file.getOriginalFilename();

            //저장 경로는
            Path savePath = Paths.get(uploadPath, savedName);

            try {
                Files.copy(file.getInputStream(), savePath); // 원본 파일 업로드

                String contentType = file.getContentType(); // Mime type

                //이미지 파일이라면
                if(contentType != null || contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + savedName);
                    Thumbnails.of(savePath.toFile()). size(200, 200).toFile(thumbnailPath.toFile());
                }

                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }// end for

        return uploadNames;
        
    }

    public ResponseEntity<Resource> getFile(String fileName){
        Resource resource = new FileSystemResource(uploadPath + File.separator+ fileName);

        if(!resource.isReadable()){
            resource = new FileSystemResource(uploadPath + File.separator+ "default.jpg");
        }

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().headers(headers).body(resource);

    }

    public void deleteFiles(List<String> fileNames){
        if(fileNames == null || fileNames.size() == 0) {
            return;
        }

        fileNames.forEach(fileName -> {

            //Thumbnail 삭제
            String thubnailFileName = "s_" + fileName;

            Path thumbnailPath = Paths.get(uploadPath, thubnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        });
    }

}
