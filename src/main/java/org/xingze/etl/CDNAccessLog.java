package org.xingze.etl;

/**
 * @program: G9-02
 * @author: huzekang
 * @create: 2020-08-02 09:15
 **/
public class CDNAccessLog {

    private String ip;
    private String protocol;
    private String domain;
    private String path;
    private String proxyIp;
    private String responseTime;
    private String refer;
    private String method;
    private String accessUrl;
    private String httpCode;
    private String requestSize;
    private long responseSize;
    private String cacheStatus;
    private String ua;
    private String fileType;
    private String province;
    private String city;
    private String isp;
    private String year;
    private String month;
    private String day;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public String getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(String requestSize) {
        this.requestSize = requestSize;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public String getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(String cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return

                ip + "\t" + protocol +
                        "\t" + domain +
                        "\t" + path +
                        "\t" + proxyIp +
                        "\t" + responseTime +
                        "\t" + refer +
                        "\t" + method +
                        "\t" + accessUrl +
                        "\t" + httpCode +
                        "\t" + requestSize +
                        "\t" + responseSize +
                        "\t" + cacheStatus +
                        "\t" + ua +
                        "\t" + fileType +
                        "\t" + province +
                        "\t" + city +
                        "\t" + isp +
                        "\t" + year +
                        "\t" + month +
                        "\t" + day
                ;
    }


}


