package test.ivacompany.com.testappcamerarecycler.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.ivacompany.com.testappcamerarecycler.R;
import test.ivacompany.com.testappcamerarecycler.TestApp;
import test.ivacompany.com.testappcamerarecycler.adapters.PhotoRecyclerViewAdapter;
import test.ivacompany.com.testappcamerarecycler.models.Photo;
import test.ivacompany.com.testappcamerarecycler.utils.Constants;
import test.ivacompany.com.testappcamerarecycler.utils.RecycleViewItemDecoration;
import test.ivacompany.com.testappcamerarecycler.utils.Utils;

/**
 * Created by root on 06.02.17.
 */

public class MainFragment extends Fragment {

    public static final String TAG = "MainFragment";

    @BindView(R.id.photoRecycler)
    RecyclerView photoRecycler;
    @BindView(R.id.openCamera)
    FloatingActionButton openCamera;

    PhotoRecyclerViewAdapter adapter;

    private static DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new PhotoRecyclerViewAdapter(Utils.readFromDB());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                TestApp.getAppContext()
        );
        photoRecycler.setLayoutManager(mLayoutManager);
        photoRecycler.addItemDecoration(
                new RecycleViewItemDecoration(
                        TestApp.getAppContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
        photoRecycler.setAdapter(adapter);
    }

    @OnClick(R.id.openCamera)
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, Constants.IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMAGE_CAPTURE && data != null) {
            Bundle extras = data.getExtras();
            addNewPhotoItem(new Photo(
                            (Bitmap) extras.get("data"),
                            formatter.format(new Date()),
                            "Photo # " + (adapter.getItemCount() + 1)
                    )
            );
        }
    }

    private void addNewPhotoItem(Photo photo) {
        Utils.addToDB(photo);
        adapter.addItem(photo);
    }
}
