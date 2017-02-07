package test.ivacompany.com.testappcamerarecycler.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.ivacompany.com.testappcamerarecycler.R;
import test.ivacompany.com.testappcamerarecycler.models.Photo;

/**
 * Created by root on 06.02.17.
 */

public class PhotoRecyclerViewAdapter
        extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoHolder> {

    private List<Photo> photoList;

    public PhotoRecyclerViewAdapter(List<Photo> photoList) {
        this.photoList = photoList;
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

        holder.itemPhoto.setImageBitmap(photoModel.getImage());
        holder.itemDate.setText(photoModel.getDate());
        holder.itemName.setText(photoModel.getName());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void addItem(Photo photoModel) {
        photoList.add(photoModel);
        notifyItemInserted(photoList.size() - 1);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemPhoto) ImageView itemPhoto;
        @BindView(R.id.itemDate) TextView itemDate;
        @BindView(R.id.itemName) TextView itemName;

        public PhotoHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
