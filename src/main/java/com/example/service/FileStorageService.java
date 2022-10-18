package com.example.service;

import com.example.entity.enums.FileStorageStatus;
import com.example.entity.model.FileStorage;
import com.example.repository.FileStorageRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileStorageService {
    //
    @Autowired
    private FileStorageRepository fileStorageRepository;

    private final Hashids hashids;

    @Value("${upload.server.folder}")
    private String serverPathLocation;

    public FileStorageService() {
        this.hashids = new Hashids(getClass().getName(), 6);
    }

    public FileStorage save(MultipartFile multipartFile){
        FileStorage fileStorage = new FileStorage();
        fileStorage.setName(multipartFile.getOriginalFilename());
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorage.setExtension(ext(multipartFile.getOriginalFilename()));
        fileStorage = fileStorageRepository.save(fileStorage);
        String path = createDateFolder();
        File uploadFolder = new File(path);
        if(!uploadFolder.exists() && uploadFolder.mkdirs()){
            System.out.println("create Folders");
        }
        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        Date now = new Date();
        String pathLocal = String.format("/upload_files/%d/%d/%d/%s.%s", 1900 + now.getYear(),
                1 + now.getMonth(), now.getDate(), fileStorage.getHashId(), fileStorage.getExtension());
        fileStorage.setUploadFolder(pathLocal);
        uploadFolder = uploadFolder.getAbsoluteFile();
        File file = new File(uploadFolder, String.format("%s.%s", fileStorage.getHashId(), fileStorage.getExtension()));
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileStorageRepository.save(fileStorage);
    }

    public String createDateFolder(){
        Date now = new Date();
        String path = String.format("%s/upload_files/%d/%d/%d", this.serverPathLocation, 1900 + now.getYear(),
                1 + now.getMonth(), now.getDate());
        return path;
    }

    public FileStorage findByHashId(String hashId){
        return fileStorageRepository.findByHashId(hashId);
    }
    public void delete(String hashId){
        FileStorage fileStorage = fileStorageRepository.findByHashId(hashId);
        File file = new File(String.format("%s/%s", this.serverPathLocation, fileStorage.getUploadFolder()));
        if(file.delete()){
            fileStorageRepository.delete(fileStorage);
        }
    }
    public String ext(String fileName){
        String extName = null;
        if(fileName != null && !fileName.isEmpty()){
            int dot = fileName.lastIndexOf(".");
            if(dot > 0 && dot <= fileName.length() -2){
                extName = fileName.substring(dot+1);
            }
        }
        return extName;
    }

}
