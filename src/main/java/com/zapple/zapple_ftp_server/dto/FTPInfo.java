package com.zapple.zapple_ftp_server.dto;

public class FTPInfo {
    private String host;
    private int port;
    private String homeDir;
    private int maxDownloadRate;
    private int maxUploadRate;
    private float usableSpace;
    private float totalSpace;

    public FTPInfo(String host, int port, String homeDir, int maxDownloadRate, int maxUploadRate, long usableSpace, long totalSpace) {
        this.host = host;
        this.port = port;
        this.homeDir = homeDir;
        this.maxDownloadRate = maxDownloadRate;
        this.maxUploadRate = maxUploadRate;

        this.usableSpace = Float.parseFloat(String.format("%.2f", usableSpace * 1.0 / 1024 / 1024 / 1024));
        this.totalSpace = Float.parseFloat(String.format("%.2f", totalSpace * 1.0 / 2014 / 1024 / 1024));
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public int getMaxDownloadRate() {
        return maxDownloadRate;
    }

    public void setMaxDownloadRate(int maxDownloadRate) {
        this.maxDownloadRate = maxDownloadRate;
    }

    public int getMaxUploadRate() {
        return maxUploadRate;
    }

    public void setMaxUploadRate(int maxUploadRate) {
        this.maxUploadRate = maxUploadRate;
    }

    public float getUsableSpace() {
        return usableSpace;
    }

    public void setUsableSpace(float usedSpace) {
        this.usableSpace = usedSpace;
    }

    public float getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(float totalSpace) {
        this.totalSpace = totalSpace;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FTPInfo{");
        sb.append("host='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", homeDir='").append(homeDir).append('\'');
        sb.append(", maxDownloadRate=").append(maxDownloadRate);
        sb.append(", maxUploadRate=").append(maxUploadRate);
        sb.append(", usedSpace=").append(usableSpace);
        sb.append(", totalSpace=").append(totalSpace);
        sb.append('}');
        return sb.toString();
    }
}
