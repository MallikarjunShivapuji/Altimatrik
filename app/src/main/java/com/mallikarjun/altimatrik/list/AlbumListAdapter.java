package com.mallikarjun.altimatrik.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mallikarjun.altimatrik.R;
import com.mallikarjun.altimatrik.repository.Album;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.CustomViewHolder> {
    private List<Album> dataList;
    private Context context;

    public AlbumListAdapter(Context context, List<Album> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.album, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTrackName());//.getTitle());
        holder.artist.setText("Artist: " + dataList.get(position).getArtistName());//.setText(dataList.get(position).getTrackName());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        holder.releaseDate.setText("Release Date: " + outputFormat.format(dataList.get(position).getReleaseDate()));
        holder.collectionName.setText("Collection: " + dataList.get(position).getCollectionName());
        holder.collectionPrice.setText("Price: " + dataList.get(position).getCollectionPrice().toString() + " $");

        Glide.with(context)
                .load(dataList.get(position).getArtworkUrl100())
                .apply(centerCropTransform())
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setNewList(List<Album> newList) {
        dataList = newList;
        notifyDataSetChanged();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView title;
        TextView artist;
        TextView releaseDate;
        TextView collectionName;
        TextView collectionPrice;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            title = mView.findViewById(R.id.tvAmbumName);
            coverImage = mView.findViewById(R.id.ivAlbum);
            artist = mView.findViewById(R.id.tvartistName);
            releaseDate = mView.findViewById(R.id.tvreleaseDate);
            collectionName = mView.findViewById(R.id.tvCollectionname);
            collectionPrice = mView.findViewById(R.id.tvCollectionPrice);
        }
    }
}