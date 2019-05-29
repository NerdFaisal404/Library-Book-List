package bd.edu.mediaplayer.phonecallbook.model;

public class BookList {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OLD_ID = "old_id";
    public static final String COLUMN_NEW_ID = "new_id";
    public static final String COLUMN_BOOK_NAME = "book_name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_REMARKS = "remarks";

    private int id;
    private int oldId;
    private int newId;
    private String bookName;
    private String author;
    private String remarks;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_OLD_ID + " INTEGER,"
                    + COLUMN_NEW_ID + " INTEGER,"
                    + COLUMN_BOOK_NAME + " TEXT,"
                    + COLUMN_AUTHOR + " TEXT,"
                    + COLUMN_REMARKS + " TEXT"
                    + ")";

    public BookList() {
    }

    public BookList(int id, int oldId, int newId, String bookName, String author, String remarks) {
        this.id = id;
        this.oldId = oldId;
        this.newId = newId;
        this.bookName = bookName;
        this.author = author;
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
