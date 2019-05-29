package bd.edu.mediaplayer.phonecallbook.ui.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bd.edu.mediaplayer.phonecallbook.R;
import bd.edu.mediaplayer.phonecallbook.db.DatabaseHelper;
import bd.edu.mediaplayer.phonecallbook.helper.CSVFile;
import bd.edu.mediaplayer.phonecallbook.model.BookList;
import bd.edu.mediaplayer.phonecallbook.model.WeatherSample;
import bd.edu.mediaplayer.phonecallbook.ui.activity.NavigationActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseBookFragment extends Fragment {


    private Button btnBrowse;
    private View view;
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressdialog;
    private Thread thread;
    private volatile boolean running = true;

    public BrowseBookFragment() {
        // Required empty public constructor
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_browse_book, container, false);
        initView();
        return view;
    }

    private void initView() {
        btnBrowse = view.findViewById(R.id.btn_browse);

        databaseHelper = new DatabaseHelper(getContext());

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                openFileChooser();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();
            }
        });
    }

    private void openFileChooser() {
        Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
        intent4.putExtra(Constant.MAX_NUMBER, 9);/*new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf", "csv"}*/
        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"csv"});
        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    StringBuilder builder = new StringBuilder();
                    for (NormalFile file : list) {
                        String path = file.getPath();
                        //builder.append(path + "\n");
                    }

                    progressdialog = new ProgressDialog(getActivity());
                    progressdialog.setMessage("Please Wait....");
                    progressdialog.show();
                    progressdialog.setCancelable(false);
                    runInBackground(list.get(0).getPath());

                }
                break;
        }

    }


    private void runInBackground(final String path) {

        thread = new Thread() {

            @Override
            public void run() {
                if (!running) {
                    return;
                } else {
                    try {
                        readFileData(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

    }

    private void readFileData(String path) throws FileNotFoundException {

        String[] data;
        File file = new File(path);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                String csvLine;
                while ((csvLine = br.readLine()) != null) {
                    data = csvLine.split(",");
                    try {
                        BookList bookList = new BookList();
                        bookList.setBookName(data[0]);
                        bookList.setAuthor(data[1]);
                        //bookList.setRemarks(data[2]);
                        databaseHelper.insertBook(bookList);
                    } catch (Exception e) {
                    }
                }
                running = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!running) {
                            progressdialog.dismiss();
                            Toast.makeText(getContext(),"BookList Successfully Updated",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: " + ex);
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "file not exists", Toast.LENGTH_SHORT).show();
        }
    }
}
