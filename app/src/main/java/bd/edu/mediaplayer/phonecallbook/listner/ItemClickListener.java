package bd.edu.mediaplayer.phonecallbook.listner;

import android.view.View;

import bd.edu.mediaplayer.phonecallbook.model.BookList;

public interface ItemClickListener  {
    void itemClick(View view, BookList bookList, int position);
}
