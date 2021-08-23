package com.zapple.zapple_ftp_server.service;

import com.zapple.zapple_ftp_server.dao.FtpServerUserDAO;
import com.zapple.zapple_ftp_server.dto.FTPInfo;
import com.zapple.zapple_ftp_server.dto.UserInfo;
import com.zapple.zapple_ftp_server.util.Properties;
import com.zapple.zapple_ftp_server.util.PropertiesHelper;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class MyFtpServer {
    private static final Logger logger = LoggerFactory.getLogger(MyFtpServer.class);

    @Autowired
     FtpServerUserDAO ftpServerUserDAO;

    private FtpServer ftpServer;
    private UserManager userManager;

    private static final String CONFIG_FILE_NAME = "application.properties";
    private static final String USERS_FILE_NAME = "users.properties";
    private static final int MAX_IDLE_TIME = 300;

    @Value("${server.host}")
    private String host;
    @Value("${server.port.ftp}")
    private int port;
    @Value("${ftp.passive-ports}")
    private String passivePorts;
    @Value("${ftp.max-logins}")
    private Integer maxLogins;
    @Value("${ftp.max-threads}")
    private Integer maxThreads;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.home-dir}")
    private String homeDir;

    public MyFtpServer(FtpServerUserDAO ftpServerUserDAO) {
        this.ftpServerUserDAO = ftpServerUserDAO;
    }

    @PostConstruct
    private void start() {
        makeHomeDir(homeDir);
        try {
            createConfigFile();
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
        logger.info("[{}, {}] creation was successful", homeDir, CONFIG_FILE_NAME);

        FtpServerFactory ftpServerFactory = new FtpServerFactory();

        //FTP service connection configuration
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(false);
        connectionConfigFactory.setMaxLogins(maxLogins);
        connectionConfigFactory.setMaxThreads(maxThreads);
        ftpServerFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());

        ListenerFactory listenerFactory = new ListenerFactory();
        //Configure FTP port
        listenerFactory.setPort(port);

        //Passive mode configuration (on demand)
        if(!Objects.equals(passivePorts,"")) {
            DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
            logger.info("Perform passive mode configuration, passive port number range : {}", passivePorts);
            dataConnectionConfigurationFactory.setPassivePorts(passivePorts);
            if(!(Objects.equals(host, "localhost") || Objects.equals(host, "127.0.0.1"))) {
                logger.info("Perform passive mode configuration, local address:{}", host);
                dataConnectionConfigurationFactory.setPassiveExternalAddress(host);
            }
            listenerFactory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        }
        ftpServerFactory.addListener("default", listenerFactory.createListener());

        //Configure custom user events
        Map<String, Ftplet> ftpletMap = new HashMap<>();
        ftpletMap.put("ftpService", new MyFtplet());
        ftpServerFactory.setFtplets(ftpletMap);

        //Set up user control center
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(USERS_FILE_NAME));
        userManagerFactory.setAdminName(username);
        userManager = userManagerFactory.createUserManager();

        try {
            initUser();
        } catch (FtpException exception) {
            logger.error(exception.getMessage());
        }
        ftpServerFactory.setUserManager(userManager);

        //Create and start the FTP service
        ftpServer = ftpServerFactory.createServer();
        try {
            ftpServer.start();
        } catch (FtpException exception) {
            logger.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
        logger.info("ftp started successfully, port number:{}",port);
    }

    @PreDestroy
    private void stop() {
        if(ftpServer != null) {
            ftpServer.stop();
        }
    }

    private void initUser() throws FtpException {

        List<Map<String, ?>> userList = ftpServerUserDAO.selectFtpUserList();

        boolean existed = userManager.doesExist(username);
        List<Authority> authorities = new ArrayList<Authority>();
        if(!existed) {
            authorities.add(new WritePermission());
            authorities.add(new ConcurrentLoginPermission(0,0));
            BaseUser user = new BaseUser();
            user.setName(username);
            user.setPassword(password);
            user.setHomeDirectory(homeDir);
            user.setMaxIdleTime(MAX_IDLE_TIME);
            user.setAuthorities(authorities);
            userManager.save(user);
        }
        logger.info("{} is initialized", authorities.toString());
    }

    public void setPassword(UserInfo userInfo) throws FtpException {
        String adminName = userManager.getAdminName();
        User saveUser = userManager.authenticate(new UsernamePasswordAuthentication(adminName,userInfo.getPassword()));
        BaseUser baseUser = new BaseUser(saveUser);
        baseUser.setPassword(userInfo.getPassword());
        userManager.save(baseUser);
    }

    public void setHomeDir(String homeDir) throws FtpException, IOException {
        User user = userManager.getUserByName(userManager.getAdminName());
        BaseUser baseUser = new BaseUser(user);
        makeHomeDir(homeDir);
        baseUser.setHomeDirectory(homeDir);
        userManager.save(baseUser);

        Properties properties = PropertiesHelper.getProperties(CONFIG_FILE_NAME);
        if(!homeDir.endsWith("/")) {
            homeDir += "/";
        }
        properties.setProperty("ftp.home-dir", homeDir);
        PropertiesHelper.setProperties(properties, CONFIG_FILE_NAME);
     }

    /**
     * Modify the maximum download speed
     * @param maxDownloadRate Maximum download speed, unit KB
     * @throws FtpException FTP exception
     */
    public void setMaxDownloadRate(int maxDownloadRate) throws FtpException {
        int maxUploadRate = getFTPInfo().getMaxUploadRate();
        saveTransferRateInfo(maxUploadRate*1024, maxDownloadRate*1024);
    }

    /**
     * Modify the maximum upload speed
     * @param maxUploadRate Maximum upload speed, unit KB
     * @throws FtpException FTP exception
     */
    public void setMaxUploadRate(int maxUploadRate) throws FtpException {
        int maxDownloadRate = getFTPInfo().getMaxDownloadRate();
        saveTransferRateInfo(maxUploadRate*1024, maxUploadRate*1024);
    }

    /**
     * Save transfer rate limit information
     * @param maxUploadRate     Maximum upload speed, unit KB
     * @param maxDownloadRate   Maximum download speed, unit KB
     * @throws FtpException FTP exception
     */
    private void saveTransferRateInfo(int maxUploadRate, int maxDownloadRate) throws FtpException {
        User userInfo = userManager.getUserByName(userManager.getAdminName());
        BaseUser baseUser = new BaseUser(userInfo);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission());
        authorities.add(new TransferRatePermission(maxDownloadRate, maxUploadRate));
        baseUser.setAuthorities(authorities);
        userManager.save(baseUser);
    }

    /**
     * Get FTP information
     * @return FTP information
     * @throws FtpException
     */
    public FTPInfo getFTPInfo() throws FtpException {
        User userInfo = userManager.getUserByName(userManager.getAdminName());
        TransferRateRequest transferRateRequest = (TransferRateRequest) userInfo.authorize(new TransferRateRequest());
        File homeDir = Paths.get(userInfo.getHomeDirectory()).toFile();
        long totalSpace = homeDir.getTotalSpace();
        long usableSpace = homeDir.getUsableSpace();

        return new FTPInfo(host, port, homeDir.getAbsolutePath(), transferRateRequest.getMaxDownloadRate()/1024,transferRateRequest.getMaxUploadRate()/1024, usableSpace, totalSpace);
    }

    private void makeHomeDir(String homeDir) {
        try {
            Files.createDirectories(Paths.get(homeDir, "temp"));
        } catch (IOException exception) {
            logger.error(exception.getMessage());
            throw new UncheckedIOException(exception);
        }
    }

    private void createConfigFile() throws IOException {
        File configFile = new File(CONFIG_FILE_NAME);
        if(!configFile.exists()) {
            boolean result = configFile.createNewFile();
            if(!result) {
                logger.error("Failed to create {}", CONFIG_FILE_NAME);
            }
        }
        File userFile = new File(USERS_FILE_NAME);
        if(!userFile.exists()) {
            boolean result = userFile.createNewFile();
            if(!result) {
                logger.error("Failed to create {}", USERS_FILE_NAME);
            }
        }
    }
}
