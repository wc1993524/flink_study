package com.soul.domain;

import java.util.Date;

/**
 * @author 若泽数据-soulChun
 * @create 2018-12-19-18:26
 */
public class CleanLog {
    private String IP;
    private String TIME;
    private String CourseID;
    private String Status_Code;
    private String Referer;

    public CleanLog(String IP, String TIME, String courseID, String status_Code, String referer) {
        this.IP = IP;
        this.TIME = TIME;
        CourseID = courseID;
        Status_Code = status_Code;
        Referer = referer;
    }

    public CleanLog() {
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    public String getStatus_Code() {
        return Status_Code;
    }

    public void setStatus_Code(String status_Code) {
        Status_Code = status_Code;
    }

    public String getReferer() {
        return Referer;
    }

    public void setReferer(String referer) {
        Referer = referer;
    }
}
