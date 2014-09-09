package com.webber.webber.db;

/**
 * Created by AndrewYao on 2014/9/1.
 */
public class Relation {
    String uid;
    String pid;

    public Relation() {
    }


    public Relation(String uid, String pid) {
        this.uid = uid;
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
