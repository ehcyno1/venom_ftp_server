package com.zapple.zapple_ftp_server.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "EPGData")
public class EPGData {
    private String EventDataCount;
    private String CHName;
    private List<EventInfo> EventInfoList;

    public EPGData() {}

    public EPGData(String EventDataCount, String CHName, List<EventInfo> EventInfoList) {
        this.EventDataCount = EventDataCount;
        this.CHName = CHName;
        this.EventInfoList = EventInfoList;
    }

    @XmlElement
    public String getEventDataCount() {
        return EventDataCount;
    }

    public void setEventDataCount(String eventDataCount) {
        EventDataCount = eventDataCount;
    }

    @XmlElement
    public String getCHName() {
        return CHName;
    }

    public void setCHName(String CHName) {
        this.CHName = CHName;
    }

    @XmlElement
    public List<EventInfo> getEventInfoList() {
        return EventInfoList;
    }

    public void setEventInfoList(List<EventInfo> eventInfoList) {
        EventInfoList = eventInfoList;
    }
}
