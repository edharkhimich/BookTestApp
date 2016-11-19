package com.appleeeee.bookstestapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appleeeee.bookstestapp.R;
import com.appleeeee.bookstestapp.model.Item;
import com.appleeeee.bookstestapp.model.VolumeInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    private Context context;
    private List<Item> itemList;
    private Item item;
    private VolumeInfo volumeInfo;

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public BookAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookHolder holder, int position) {
        if (itemList.get(position) != null) {
            item = itemList.get(position);
            holder.bind(item, holder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_holder)
        LinearLayout bookHolder;

        @BindView(R.id.book_image)
        ImageView bookImage;

        @BindView(R.id.book_title)
        TextView bookTitle;

        public String infoLink;

        public BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Item item, final BookHolder holder) {
            volumeInfo = item.getVolumeInfo();
            if (volumeInfo.getImageLinks() != null) {
                Picasso.with(context).load(item.getVolumeInfo()
                        .getImageLinks()
                        .getThumbnail())
                        .into(holder.bookImage);
            } else {
                Picasso.with(context).load(R.drawable.picture)
                        .resize(300, 450)
                        .into(holder.bookImage);
            }
            infoLink = item.getVolumeInfo().getInfoLink();
            bookTitle.setText(item.getVolumeInfo().getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(infoLink));
                    context.startActivity(browse);

                }
            });
        }
    }
}