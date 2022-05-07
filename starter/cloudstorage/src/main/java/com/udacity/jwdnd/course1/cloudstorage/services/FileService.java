package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int createFile(File file, MultipartFile multipartFile) throws Exception {
        file.setFileName(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        file.setFileSize(String.valueOf(multipartFile.getSize()));
        file.setFileData(multipartFile.getBytes());

        return fileMapper.addFile(file);
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFileByName(String fileName) {
        return fileMapper.getFile(fileName);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFileId(fileId);
    }
}
