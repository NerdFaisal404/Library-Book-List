package bd.edu.mediaplayer.phonecallbook.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bd.edu.mediaplayer.phonecallbook.R;
import bd.edu.mediaplayer.phonecallbook.db.DatabaseHelper;
import bd.edu.mediaplayer.phonecallbook.model.BookList;


public class AddBookFragment extends Fragment implements View.OnClickListener {

    private View view;

    private EditText edtBookName, edtAuthorName, edtReference;
    private Button btnSubmit;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_book, container, false);
        initView();
        return view;
    }

    private void initView() {
        edtBookName = view.findViewById(R.id.edt_book_name);
        edtAuthorName = view.findViewById(R.id.edt_author_name);
        edtReference = view.findViewById(R.id.edt_reference);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (!TextUtils.isEmpty(edtBookName.getText().toString()) &&
                        !TextUtils.isEmpty(edtAuthorName.getText().toString())) {

                    BookList bookList = new BookList();
                    bookList.setBookName(edtBookName.getText().toString());
                    bookList.setAuthor(edtAuthorName.getText().toString());

                    if (!TextUtils.isEmpty(edtReference.getText().toString()))
                        bookList.setRemarks(edtReference.getText().toString());
                    databaseHelper.insertBook(bookList);
                    Toast.makeText(getActivity(), "Successfully Book Added", Toast.LENGTH_SHORT).show();
                    edtBookName.setText("");
                    edtAuthorName.setText("");
                    edtReference.setText("");

                } else {
                    Toast.makeText(getActivity(), "Please Fill Book Name and Author ", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
