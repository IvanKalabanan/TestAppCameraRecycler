package test.ivacompany.com.testappcamerarecycler.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.ivacompany.com.testappcamerarecycler.R;
import test.ivacompany.com.testappcamerarecycler.models.Photo;
import test.ivacompany.com.testappcamerarecycler.utils.Constants;
import test.ivacompany.com.testappcamerarecycler.utils.Utils;

/**
 * Created by iva on 07.02.17.
 */

public class PreviewFragment extends Fragment {

    @BindView(R.id.photoPreview)
    ImageView photoPreview;
    @BindView(R.id.photoName)
    TextView photoName;
    @BindView(R.id.photoDate)
    TextView photoDate;
    @BindView(R.id.removePhoto)
    FrameLayout removePhoto;

    private List<Photo> photoList;
    private long currentPhotoId;

    public static PreviewFragment newInstance(long photoId) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ID, photoId);
        previewFragment.setArguments(bundle);
        return previewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preview, container, false);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        ButterKnife.bind(this, root);

        currentPhotoId = getArguments().getLong(Constants.ID);
        photoPreview.setTransitionName(Utils.getTransitionName(currentPhotoId));
        Photo p = Utils.getPhoto(currentPhotoId);

        photoPreview.setImageURI(p.getImageUri());
        photoDate.setText(p.getDate());
        photoName.setText(p.getName());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        photoList = Utils.readFromDB();
    }

    @OnClick(R.id.removePhoto)
    public void removePhotoIconClick() {
        if (photoList.size() == 1) {
            Utils.removeFromRealm(currentPhotoId);
            getActivity().onBackPressed();
            return;
        }
        Photo newPhoto;
        for (int i = 0; i < photoList.size(); i++) {
            long photoId = photoList.get(i).getId();
            if (i == (photoList.size() - 1)) {
                newPhoto = photoList.get(0);
                Utils.removeFromRealm(currentPhotoId);
                currentPhotoId = newPhoto.getId();
                updatePhoto(newPhoto);
                photoList.remove(i);
                break;
            }
            if (photoId == currentPhotoId) {
                Utils.removeFromRealm(currentPhotoId);
                newPhoto = photoList.get(i + 1);
                currentPhotoId = newPhoto.getId();
                updatePhoto(newPhoto);
                photoList.remove(i);
                break;
            }
        }
    }

    private void updatePhoto(Photo p) {
        photoPreview.setTransitionName(Utils.getTransitionName(currentPhotoId));
        photoPreview.setImageURI(p.getImageUri());
        photoDate.setText(p.getDate());
        photoName.setText(p.getName());
    }

}
