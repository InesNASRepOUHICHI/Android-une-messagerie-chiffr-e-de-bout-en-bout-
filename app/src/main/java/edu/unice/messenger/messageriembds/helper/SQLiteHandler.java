package edu.unice.messenger.messageriembds.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.unice.messenger.messageriembds.Model.User;

public class SQLiteHandler extends SQLiteOpenHelper {
 
    private static final String TAG = SQLiteHandler.class.getSimpleName();
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MBDS-V1";

    // Login table name
    private static final String TABLE_USER = "user";
    // Contact table name
    private static final String TABLE_CONTACT = "contact";
    // Message table name
    private static final String TABLE_MESSAGE = "message";
 
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_MESSAGE = "message";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
                + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT," + KEY_ACCESS_TOKEN + " TEXT" +")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
                + " TEXT UNIQUE" +")";
        db.execSQL(CREATE_CONTACT_TABLE);

        String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME
                + " TEXT," + KEY_MESSAGE + " TEXT" +")";
        db.execSQL(CREATE_MESSAGE_TABLE);
 
        Log.d(TAG, "Database tables created");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername()); // username
        values.put(KEY_PASSWORD, user.getPassword()); // password
        values.put(KEY_ACCESS_TOKEN, user.getAccess_token()); // access_token

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    public void addContact(String contactUserName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, contactUserName); // username

        // Inserting Row
        long id = db.insert(TABLE_CONTACT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New contact inserted into sqlite: " + id);
    }

    public List<String> getContacts() {

        List<String> contacts = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;

        Cursor cursor = db.rawQuery(selectQuery,null,null);
        ArrayList dataModelArrayList = new ArrayList();
        if(cursor.moveToFirst()){
            do {
                contacts.add(cursor.getString(cursor.getColumnIndex("username")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public void addMessage(String messageBody) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, messageBody); // username

        // Inserting Row
        long id = db.insert(TABLE_MESSAGE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New contact inserted into sqlite: " + id);
    }
 
    /**
     * Getting user data from database
     * */
    public User getUserDetails() {
        User user = new User();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setAccess_token(cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        ContactUtils contactUtils = new ContactUtils();
        contactUtils.setUsernameConnected(user.getUsername());

        return user;
    }
 
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_CONTACT, null, null);
        db.delete(TABLE_MESSAGE, null, null);

        db.close();

        Log.d(TAG, "Deleted all tables info from sqlite");
    }
 
}