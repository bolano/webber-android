package com.webber.webber.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndrewYao on 2014/8/31.
 */
public class WebberDBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WebberDB";

    private static final String KEY_ID = "_id";
    private static final String PERSON_TABLE = "WEBBER_PERSON_TABLE";
    /*
    * uid, realname, pid,company, division,position,news_date,news_title
     */
    private static final String TABLE_PERSON_CREATE = "create table " +
            PERSON_TABLE + "(" + KEY_ID +
            " integer primary key autoincrement, " +
            "pid text not null, " +
            "realname text not null, " +
            "company text not null, " +
            "division text," +
            "position text," +
            "news_date text," +
            "news_title text, " +
            "address text, " +
            "cellphone text, " +
            "telephone text, " +
            "email text " +
            ");";
    private static final String RELATION_TABLE = "WEBBER_RELATION_TABLE";
    private static final String TABLE_RELATION_CREATE = "create table " +
            RELATION_TABLE + "(" + KEY_ID +
            " integer primary key autoincrement, " +
            "uid text not null, " +
            "pid text not null);";
    private static final int DATABASE_VERSION = 1;

    public WebberDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public WebberDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_PERSON_CREATE);
        db.execSQL(TABLE_RELATION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    public void addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("pid", person.getPid());
        values.put("realname", person.getRealname());
        values.put("company", person.getCompany());
        values.put("division", person.getDivision());
        values.put("position", person.getPosition());
        values.put("news_date", person.getNews_date());
        values.put("news_title", person.getNews_title());
        values.put("address", person.getAddress());
        values.put("telephone", person.getTelephone());
        values.put("cellphone", person.getCellphone());
        values.put("email", person.getEmail());

        db.insert(PERSON_TABLE, null, values);
        db.close();

        Log.v("DB", person.getRealname());
    }

    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<Person>();
        String selectQuery = "SELECT * FROM " + PERSON_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setPid(cursor.getString(1));
                person.setRealname(cursor.getString(2));
                person.setCompany(cursor.getString(3));
                person.setDivision(cursor.getString(4));
                person.setPosition(cursor.getString(5));
                person.setNews_date(cursor.getString(6));
                person.setNews_title(cursor.getString(7));
                person.setAddress(cursor.getString(8));
                person.setCellphone(cursor.getString(9));
                person.setTelephone(cursor.getString(10));
                person.setEmail(cursor.getString(11));

                personList.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return personList;
    }

    public Person getPerson(String pid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(PERSON_TABLE,
                new String[]{"pid", "realname", "company", "division", "position", "news_date", "news_title", "address", "cellphone", "telephone", "email"},
                "pid=?",
                new String[]{pid}, null, null, null, null);

        if (cursor.moveToFirst()) {
            Person person = new Person(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
            cursor.close();
            return person;
        } else {
            cursor.close();
            return null;
        }

    }

    public List<Person> getPerson(String[] pid) {

        Log.v("DB getPerson", String.valueOf(pid.length));
/*
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(PERSON_TABLE,
                new String[]{"pid","realname","company","division","position","news_date","news_title"},
                "pid=?",
                pid, null, null, null, null);
*/
        List<Person> personList = new ArrayList<Person>();

        for (int i = 0; i < pid.length; i++) {
            personList.add(getPerson(pid[i]));
        }
/*
        if(cursor.moveToFirst()){
            do {
                Person person = new Person();

                person.setPid(cursor.getString(0));
                person.setRealname(cursor.getString(1));
                person.setCompany(cursor.getString(2));
                person.setDivision(cursor.getString(3));
                person.setPosition(cursor.getString(4));
                person.setNews_date(cursor.getString(5));
                person.setNews_title(cursor.getString(6));

                personList.add(person);
            }while(cursor.moveToNext());
        }

        cursor.close();
*/
        return personList;
    }

    public int getPersonCount() {
        String selectQuery = "SELECT * FROM " + PERSON_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("pid", person.getPid());
        values.put("realname", person.getRealname());
        values.put("company", person.getCompany());
        values.put("division", person.getDivision());
        values.put("position", person.getPosition());
        values.put("news_date", person.getNews_date());
        values.put("news_title", person.getNews_title());
        values.put("address", person.getAddress());
        values.put("telephone", person.getTelephone());
        values.put("cellphone", person.getCellphone());
        values.put("email", person.getEmail());

        return db.update(PERSON_TABLE, values, "pid=?", new String[]{person.getPid()});

    }

    public void deletePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PERSON_TABLE, "pid=?", new String[]{person.getPid()});
        db.close();
    }

    public void addRelation(Relation relation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("uid", relation.getUid());
        values.put("pid", relation.getPid());

        db.insert(RELATION_TABLE, null, values);
        db.close();
    }

    public List<String> getAllFriendsID(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RELATION_TABLE,
                new String[]{"pid"},
                "uid=?",
                new String[]{uid}, null, null, null, null);

        List<String> friendsPID = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                friendsPID.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return friendsPID;
    }

    public List<Person> getAllFriends(String uid) {
        Log.v("DB all person count", String.valueOf(getPersonCount()));
        List<String> friendsID = getAllFriendsID(uid);
        Log.v("DB getAllFriends", String.valueOf(friendsID.size()));
        return getPerson(friendsID.toArray(new String[friendsID.size()]));
    }
}
