package com.zapple.zapple_ftp_server.service;

import com.zapple.zapple_ftp_server.dto.EPGData;
import org.apache.ftpserver.ftplet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

public class MyFtplet extends DefaultFtplet {
    private static final Logger logger = LoggerFactory.getLogger(MyFtplet.class);

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        //Get the upload path of the uploaded file
        String path = session.getUser().getHomeDirectory();
        String name = session.getUser().getName();
        String fileName = request.getArgument();
        logger.info("USER:'{}'，Upload files to the directory：'{}'，The file name is：'{}'，Status: uploading started", name, path, fileName);

        return super.onUploadStart(session, request);
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        //Get the upload path of the uploaded file
        String path = session.getUser().getHomeDirectory();
        String name = session.getUser().getName();
        String fileName = request.getArgument();
        try {
            File file = new File(path + fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(EPGData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            EPGData epgData = (EPGData) unmarshaller.unmarshal(file);
            logger.info("EPGData.CHName : {}", epgData.getCHName());
        } catch (JAXBException exception) {
            logger.error(exception.getMessage());
        }

        logger.info("USER:'{}'，Upload files to the directory：'{}'，The file name is：'{}'，Status: Status: Success!", name, path, fileName);
        return super.onUploadEnd(session, request);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadStart(session, request);
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadEnd(session, request);
    }


}
