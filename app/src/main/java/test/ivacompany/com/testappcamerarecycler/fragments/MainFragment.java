package test.ivacompany.com.testappcamerarecycler.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import test.ivacompany.com.testappcamerarecycler.views.PhotoTransition;

import static android.R.attr.width;
import static android.app.Activity.RESULT_OK;
import static java.lang.System.out;

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
    private Uri photoURI;
    private String photoName;

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

        Slide slideTransition = new Slide(Gravity.RIGHT);
        slideTransition.setDuration(500);

        ChangeImageTransform changeBoundsTransition = new ChangeImageTransform();
        changeBoundsTransition.setDuration(500);

        this.setAllowReturnTransitionOverlap(true);
        this.setExitTransition(new Fade());
        this.setSharedElementReturnTransition(new PhotoTransition());
    }

    private void initRecyclerView() {
        adapter = new PhotoRecyclerViewAdapter(
                getContext(),
                Utils.readFromDB());
        GridLayoutManager mLayoutManager = new GridLayoutManager(
                TestApp.getAppContext(),
                2
        );
        photoRecycler.setLayoutManager(mLayoutManager);

        ItemTouchHelper swipeToDelete = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        Utils.removeFromRealm(adapter.getItem(viewHolder.getAdapterPosition()).getId());
                        adapter.removeItem(viewHolder.getAdapterPosition());
                    }
                });

        swipeToDelete.attachToRecyclerView(photoRecycler);

        photoRecycler.setAdapter(adapter);
    }

    @OnClick(R.id.openCamera)
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("ssMM").format(new Date());
        photoName = "Photo_" + timeStamp + ".jpg";
        photoURI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), photoName));
        if (photoURI != null) {
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, Constants.IMAGE_CAPTURE);
        } else {
            Toast.makeText(
                    getContext(),
                    getString(R.string.file_problem),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                decodeAndSaveImage();

                addNewPhotoItem(new Photo(
                                Utils.getRealm().where(Photo.class).max(Constants.ID).longValue() + 1,
                                photoURI,
                                formatter.format(new Date()),
                                photoName
                        )
                );
            } catch (RuntimeException e) {
                addNewPhotoItem(new Photo(
                                1,
                                photoURI,
                                formatter.format(new Date()),
                                photoName
                        )
                );
            }
        }
    }

    private void decodeAndSaveImage() {
        try {
            Bitmap b = getResizeBitmap();

            ExifInterface exif = new ExifInterface(photoURI.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
            b = Bitmap.createBitmap(b, 0, 0, 600, 475, matrix, true);

            File file = new File(Environment.getExternalStorageDirectory(), photoName);
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private Bitmap getResizeBitmap() {
        try {
            Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
            int realWidth = b.getWidth(), realHeight = b.getHeight();
            if (realWidth > 600) {
                realWidth = 600;
            }
            if (realHeight > 475) {
                realHeight = 475;
            }
            return Bitmap.createScaledBitmap(b, realWidth, realHeight, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addNewPhotoItem(Photo photo) {
        Utils.addToDB(photo);
        adapter.addItem(photo);
    }
}
