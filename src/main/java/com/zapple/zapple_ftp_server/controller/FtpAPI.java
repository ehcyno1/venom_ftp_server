package com.zapple.zapple_ftp_server.controller;

import com.zapple.zapple_ftp_server.dto.FTPInfo;
import com.zapple.zapple_ftp_server.dto.UserInfo;
import com.zapple.zapple_ftp_server.service.MyFtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class FtpAPI {
    @Autowired
    private MyFtpServer ftpServer;

    @GetMapping("info")
    public FTPInfo info() throws FtpException {
        return ftpServer.getFTPInfo();
    }

    @PostMapping("setMaxUploadRate")
    public FTPInfo setMaxUploadRate(@RequestBody FTPInfo ftpInfo) throws FtpException, IOException {
        ftpServer.setMaxUploadRate(ftpInfo.getMaxUploadRate());
        return ftpServer.getFTPInfo();
    }

    @PostMapping("setMaxDownloadRate")
    public FTPInfo setMaxDownloadRate(@RequestBody FTPInfo ftpInfo) throws FtpException, IOException {
        ftpServer.setMaxDownloadRate(ftpInfo.getMaxDownloadRate());
        return ftpServer.getFTPInfo();
    }

    @PostMapping("setHomeDir")
    public FTPInfo setHomeDir(@RequestBody FTPInfo ftpInfo) throws FtpException, IOException {
        ftpServer.setHomeDir(ftpInfo.getHomeDir());
        return ftpServer.getFTPInfo();
    }

    @PostMapping("setPassword")
    public FTPInfo setPassword(@RequestBody UserInfo userInfo) throws FtpException, IOException {
        ftpServer.setPassword(userInfo);
        return ftpServer.getFTPInfo();
    }
}
