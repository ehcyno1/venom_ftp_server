package com.zapple.zapple_ftp_server.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "EPGData")
public class EPGData {
    private String EventDataCount;
    private String CHName;
    private List<EventInfo> EventInfo;

    public EPGData() {}

    public EPGData(String EventDataCount, String CHName, List<EventInfo> EventInfo) {
        this.EventDataCount = EventDataCount;
        this.CHName = CHName;
        this.EventInfo = EventInfo;
    }

    public String getEventDataCount() {
        return EventDataCount;
    }

    @XmlElement(name = "EventDataCount")
    public void setEventDataCount(String EventDataCount) {
        this.EventDataCount = EventDataCount;
    }

    public String getCHName() {
        return CHName;
    }

    @XmlElement(name = "CHName")
    public void setCHName(String CHName) {
        this.CHName = CHName;
    }

    public List<EventInfo> getEventInfoList() {
        return EventInfo;
    }

    @XmlElement(name = "EventInfo")
    public void setEventInfoList(List<EventInfo> EventInfo) {
        this.EventInfo = EventInfo;
    }
}
