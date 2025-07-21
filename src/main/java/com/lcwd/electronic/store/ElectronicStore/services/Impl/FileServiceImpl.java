package com.lcwd.electronic.store.ElectronicStore.services.Impl;

import com.lcwd.electronic.store.ElectronicStore.exception.BadApiRequest;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName= file.getOriginalFilename();
        String fileName= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension=fileName+extension;
        String fullPathWithFileName=path+ File.separator+fileNameWithExtension;
        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")){
           File folder=new File(path);
           if(!folder.exists()){
               folder.mkdirs();
           }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
           return fileNameWithExtension;

        }else{
            throw new BadApiRequest("File with this "+extension+" not allowed!!");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;

    }
}
