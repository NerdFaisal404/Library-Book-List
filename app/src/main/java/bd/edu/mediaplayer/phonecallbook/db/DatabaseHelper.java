package bd.edu.mediaplayer.phonecallbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bd.edu.mediaplayer.phonecallbook.model.BookList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(BookList.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + BookList.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public BookList getBookList(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(BookList.TABLE_NAME,
                new String[]{BookList.COLUMN_ID, BookList.COLUMN_OLD_ID, BookList.COLUMN_NEW_ID,
                        BookList.COLUMN_BOOK_NAME, BookList.COLUMN_AUTHOR, BookList.COLUMN_REMARKS},
                BookList.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare bookList object
        assert cursor != null;
        BookList bookList = new BookList(
                cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_OLD_ID)),
                cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_NEW_ID)),
                cursor.getString(cursor.getColumnIndex(BookList.COLUMN_BOOK_NAME)),
                cursor.getString(cursor.getColumnIndex(BookList.COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BookList.COLUMN_REMARKS)));

        // close the db connection
        cursor.close();

        return bookList;
    }

    public List<BookList> getAllBoks() {
        List<BookList> bookLists = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + BookList.TABLE_NAME + " ORDER BY " +
                BookList.COLUMN_BOOK_NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BookList bookList = new BookList();
                bookList.setId(cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_ID)));
                bookList.setOldId(cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_OLD_ID)));
                bookList.setNewId(cursor.getInt(cursor.getColumnIndex(BookList.COLUMN_NEW_ID)));
                bookList.setBookName(cursor.getString(cursor.getColumnIndex(BookList.COLUMN_BOOK_NAME)));
                bookList.setAuthor(cursor.getString(cursor.getColumnIndex(BookList.COLUMN_AUTHOR)));
                bookList.setRemarks(cursor.getString(cursor.getColumnIndex(BookList.COLUMN_REMARKS)));

                bookLists.add(bookList);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return bookLists list
        return bookLists;
    }

    public int getBooksCount() {
        String countQuery = "SELECT  * FROM " + BookList.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public long insertBook(BookList bookList) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(BookList.COLUMN_OLD_ID, bookList.getOldId());
        values.put(BookList.COLUMN_NEW_ID, bookList.getNewId());
        values.put(BookList.COLUMN_BOOK_NAME, bookList.getBookName());
        values.put(BookList.COLUMN_AUTHOR, bookList.getAuthor());
        values.put(BookList.COLUMN_REMARKS, bookList.getRemarks());

        // insert row
        long id = db.insert(BookList.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public int updateBook(BookList bookList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookList.COLUMN_OLD_ID, bookList.getOldId());
        values.put(BookList.COLUMN_NEW_ID, bookList.getNewId());
        values.put(BookList.COLUMN_BOOK_NAME, bookList.getBookName());
        values.put(BookList.COLUMN_AUTHOR, bookList.getAuthor());
        values.put(BookList.COLUMN_REMARKS, bookList.getRemarks());

        // updating row
        return db.update(BookList.TABLE_NAME, values, BookList.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookList.getId())});
    }

    public void deleteBook(BookList bookList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BookList.TABLE_NAME, BookList.COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookList.getId())});
        db.close();
    }
}