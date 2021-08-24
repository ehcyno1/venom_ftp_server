package com.zapple.zapple_ftp_server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FtpServerUserDAO {
    private static final Logger logger = LoggerFactory.getLogger(FtpServerUserDAO.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, ?>> selectFtpUserList() {

        return jdbcTemplate.query("SELECT userid, userpassword, homedirectory, idletime  FROM ftp_user", (rs, rowNum) -> {
            Map<String, Object> userList = new HashMap<>();
            userList.put("userid", rs.getString("userid"));
            userList.put("userpassword", rs.getString("userpassword"));
            userList.put("homedirectory", rs.getString("homedirectory"));
            userList.put("idletime", rs.getString("idletime"));

//            logger.info("FTP USER LIST : {}", userList.toString());
            return userList;
        });
    }
}
