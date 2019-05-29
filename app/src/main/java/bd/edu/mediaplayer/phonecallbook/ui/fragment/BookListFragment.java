package bd.edu.mediaplayer.phonecallbook.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import bd.edu.mediaplayer.phonecallbook.R;
import bd.edu.mediaplayer.phonecallbook.adapter.BookListAdapter;
import bd.edu.mediaplayer.phonecallbook.db.DatabaseHelper;
import bd.edu.mediaplayer.phonecallbook.listner.ItemClickListener;
import bd.edu.mediaplayer.phonecallbook.model.BookList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment implements ItemClickListener {

    private RecyclerView recyclerView;
    private View view;
    private BookListAdapter mAdapter;
    private List<BookList> bookLists;
    private EditText inputSearch;
    private DatabaseHelper databaseHelper;
    private CompositeDisposable disposable = new CompositeDisposable();


    public BookListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_list, container, false);
        initView();
        loadDataFromDb();
        return view;
    }


    private void loadDataFromDb() {
        databaseHelper = new DatabaseHelper(getActivity());

        bookLists = databaseHelper.getAllBoks();

        if (bookLists.size() > 0) {
            mAdapter = new BookListAdapter(bookLists, getContext(), this);
            recyclerView.setAdapter(mAdapter);

        }
    }

    private void initView() {
        bookLists = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_bookList);
        inputSearch = view.findViewById(R.id.input_search);


        mAdapter = new BookListAdapter(bookLists, getContext(), this);
        mAdapter.setItemClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        initSearchOversable();
    }


    private void initSearchOversable() {
        disposable.add(RxTextView.textChangeEvents(inputSearch)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                /*.filter(new Predicate<TextViewTextChangeEvent>() {
                    @Override
                    public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        return TextUtils.isEmpty(textViewTextChangeEvent.text().toString()) || textViewTextChangeEvent.text().toString().length() > 2;
                    }
                })*/
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContacts()));
    }

    private DisposableObserver<TextViewTextChangeEvent> searchContacts() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                mAdapter.getFilter().filter(textViewTextChangeEvent.text());

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void openDialogForDelete(final int position, final BookList bookList) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are You Want to Delete This Book ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mAdapter.removeAt(position);
                        databaseHelper.deleteBook(bookList);

                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void openCustomDialog(final int position, final BookList bookList) {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_custom_dialog);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText edtBookName = dialog.findViewById(R.id.edt_book_name);
        final EditText edtBookAuthor = dialog.findViewById(R.id.edt_author_name);
        final EditText edtBookReference = dialog.findViewById(R.id.edt_reference);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_submit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtBookName.getText().toString()) &&
                        !TextUtils.isEmpty(edtBookAuthor.getText().toString())) {
                    String bookName = edtBookName.getText().toString();
                    String Author = edtBookAuthor.getText().toString();
                    String Reference = edtBookReference.getText().toString();
                    BookList item = new BookList();
                    item.setBookName(Author);
                    item.setAuthor(bookName);
                    item.setRemarks(Reference);
                    item.setId(bookList.getId());
                    mAdapter.updateAt(position, item);
                    databaseHelper.updateBook(item);
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    @Override
    public void itemClick(View view, BookList bookList, int position) {
        switch (view.getId()) {
            case R.id.iv_delete:
                openDialogForDelete(position, bookList);
                break;

            case R.id.iv_edit:
                openCustomDialog(position,bookList);
                break;
        }
    }

}
