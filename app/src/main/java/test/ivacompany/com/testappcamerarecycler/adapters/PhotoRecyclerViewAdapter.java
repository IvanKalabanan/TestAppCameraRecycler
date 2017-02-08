package test.ivacompany.com.testappcamerarecycler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ivacompany.com.testappcamerarecycler.R;
import test.ivacompany.com.testappcamerarecycler.activity.MainActivity;
import test.ivacompany.com.testappcamerarecycler.fragments.MainFragment;
import test.ivacompany.com.testappcamerarecycler.fragments.PreviewFragment;
import test.ivacompany.com.testappcamerarecycler.models.Photo;
import test.ivacompany.com.testappcamerarecycler.utils.Utils;
import test.ivacompany.com.testappcamerarecycler.views.PhotoTransition;

/**
 * Created by root on 06.02.17.
 */

public class PhotoRecyclerViewAdapter
        extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoHolder> {

    private final Context context;
    private List<Photo> photoList;

    public PhotoRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_photo_recycler,
                        parent,
                        false
                );
        return new PhotoHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        Photo photoModel = photoList.get(position);

        holder.itemPhoto.setTransitionName(Utils.getTransitionName(photoModel.getId()));
        holder.itemPhoto.setImageURI(photoModel.getImageUri());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void addItem(Photo photoModel) {
        photoList.add(photoModel);
        notifyItemInserted(photoList.size() - 1);
    }

    @Override
    public long getItemId(int position) {
        return photoList.get(position).getId();
    }

    public void removeItem(int pos) {
        photoList.remove(pos);
        notifyItemRemoved(pos);
    }

    public Photo getItem(int adapterPosition) {
        return photoList.get(adapterPosition);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemPhoto)
        ImageView itemPhoto;

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewFragment previewFragment = PreviewFragment.newInstance(
                        photoList.get(getAdapterPosition()).getId()
                );

                previewFragment.setAllowEnterTransitionOverlap(true);
                previewFragment.setEnterTransition(new Fade());
                previewFragment.setSharedElementEnterTransition(new PhotoTransition());

                ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, previewFragment)
                        .addSharedElement(
                                itemPhoto,
                                Utils.getTransitionName(photoList.get(getAdapterPosition()).getId())
                        )
                        .addToBackStack(MainFragment.TAG)
                        .commit();
            }
        };

        public PhotoHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemPhoto.setOnClickListener(onClickListener);
        }
    }

}
