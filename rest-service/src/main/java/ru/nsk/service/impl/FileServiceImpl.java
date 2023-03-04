package ru.nsk.service.impl;

import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.nsk.CryptoTool;
import ru.nsk.dao.AppDocumentDAO;
import ru.nsk.dao.AppPhotoDAO;
import ru.nsk.entity.enums.AppDocument;
import ru.nsk.entity.enums.AppPhoto;
import ru.nsk.entity.enums.BinaryContent;
import ru.nsk.service.FileService;

import java.io.File;
import java.io.IOException;

@Log4j
@Service
public class FileServiceImpl implements FileService {

    private final AppPhotoDAO appPhotoDAO;
    private final AppDocumentDAO appDocumentDAO;
    private final CryptoTool cryptoTool;

    public FileServiceImpl(AppPhotoDAO appPhotoDAO, AppDocumentDAO appDocumentDAO, CryptoTool cryptoTool) {
        this.appPhotoDAO = appPhotoDAO;
        this.appDocumentDAO = appDocumentDAO;
        this.cryptoTool = cryptoTool;
    }


    @Override
    public AppDocument getDocument(String hash) {
     var id = cryptoTool.idOf(hash);
     if(id==null){
         return null;
     }
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        var id = cryptoTool.idOf(hash);
        if(id==null){
            return null;
        }
        return appPhotoDAO.findById(id).orElse(null);
    }


    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {

        try{
            File temp = File.createTempFile("tempFile", ".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        }catch (IOException e){
           log.error(e);
            return null;
        }
    }
}
