package com.webber.webber.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AndrewYao on 2014/9/1.
 */
public class Person implements Parcelable {

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            String[] stringArray = new String[11];
            in.readStringArray(stringArray);
            return new Person(stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4], stringArray[5], stringArray[6], stringArray[7], stringArray[8], stringArray[9], stringArray[10]);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    String pid;
    String realname;
    String company;
    String division;
    String position;
    String news_date;
    String news_title;
    String[] news_ids;
    String address;
    String cellphone;
    String telephone;
    String email;
    String photo_url;

    public Person() {

    }

    public Person(String pid, String realname, String company, String division, String position, String news_date, String news_title) {
        this.pid = pid;
        this.realname = realname;
        this.company = company;
        this.division = division;
        this.position = position;
        this.news_date = news_date;
        this.news_title = news_title;
    }

    public Person(String pid, String realname, String company, String division, String position, String news_date, String news_title, String address, String cellphone, String telephone, String email) {
        this.pid = pid;
        this.realname = realname;
        this.company = company;
        this.division = division;
        this.position = position;
        this.news_date = news_date;
        this.news_title = news_title;
        this.address = address;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.email = email;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String[] getNews_ids() {
        return news_ids;
    }

    public void setNews_ids(String[] news_ids) {
        this.news_ids = news_ids;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        String[] stringArray = new String[]{this.getPid(), this.getRealname(), this.getCompany(), this.getDivision(), this.getPosition(), this.getNews_date(), this.getNews_title(), this.getAddress(), this.getCellphone(), this.getTelephone(), this.getEmail()};
        parcel.writeStringArray(stringArray);

    }
}
