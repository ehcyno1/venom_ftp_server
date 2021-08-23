package com.zapple.zapple_ftp_server.dto;

public class EventInfo {
    private int EventID;
    private String EventType;
    private String EventName;
    private String EventSubName;
    private String OnAirDate;
    private String StartTime;
    private String OnairDur;
    private String EventAge;
    private String ClipID;
    private String CopyRight;
    private String Live;
    private String TargetCm;

    public EventInfo() {}
    public EventInfo(int eventID, String eventType, String eventName, String eventSubName, String onAirDate, String startTime, String onairDur, String eventAge, String clipID, String copyRight, String live, String targetCm) {
        this.EventID = eventID;
        this.EventType = eventType;
        this.EventName = eventName;
        this.EventSubName = eventSubName;
        this.OnAirDate = onAirDate;
        this.StartTime = startTime;
        this.OnairDur = onairDur;
        this.EventAge = eventAge;
        this.ClipID = clipID;
        this.CopyRight = copyRight;
        this.Live = live;
        this.TargetCm = targetCm;
    }

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventSubName() {
        return EventSubName;
    }

    public void setEventSubName(String eventSubName) {
        EventSubName = eventSubName;
    }

    public String getOnAirDate() {
        return OnAirDate;
    }

    public void setOnAirDate(String onAirDate) {
        OnAirDate = onAirDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getOnairDur() {
        return OnairDur;
    }

    public void setOnairDur(String onairDur) {
        OnairDur = onairDur;
    }

    public String getEventAge() {
        return EventAge;
    }

    public void setEventAge(String eventAge) {
        EventAge = eventAge;
    }

    public String getClipID() {
        return ClipID;
    }

    public void setClipID(String clipID) {
        ClipID = clipID;
    }

    public String getCopyRight() {
        return CopyRight;
    }

    public void setCopyRight(String copyRight) {
        CopyRight = copyRight;
    }

    public String getLive() {
        return Live;
    }

    public void setLive(String live) {
        Live = live;
    }

    public String getTargetCm() {
        return TargetCm;
    }

    public void setTargetCm(String targetCm) {
        TargetCm = targetCm;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventInfo{");
        sb.append("EventID=").append(EventID);
        sb.append(", EventType='").append(EventType).append('\'');
        sb.append(", EventName='").append(EventName).append('\'');
        sb.append(", EventSubName='").append(EventSubName).append('\'');
        sb.append(", OnAirDate='").append(OnAirDate).append('\'');
        sb.append(", StartTime='").append(StartTime).append('\'');
        sb.append(", OnairDur='").append(OnairDur).append('\'');
        sb.append(", EventAge='").append(EventAge).append('\'');
        sb.append(", ClipID='").append(ClipID).append('\'');
        sb.append(", CopyRight='").append(CopyRight).append('\'');
        sb.append(", Live='").append(Live).append('\'');
        sb.append(", TargetCm='").append(TargetCm).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
