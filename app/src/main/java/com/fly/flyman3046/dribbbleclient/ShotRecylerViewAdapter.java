package com.fly.flyman3046.dribbbleclient;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.flyman3046.dribbbleclient.model.Shot;
import com.fly.flyman3046.dribbbleclient.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShotRecylerViewAdapter extends RecyclerView.Adapter<ShotRecylerViewAdapter.ShotRecylerViewHolders> {
    private final static String TAG = ShotRecylerViewHolders.class.getSimpleName();
    private List<Shot> mShotsList;
    private Context mContext;
    private final static String TRANSIT_NAME = "imageAnimation";
    public final static String IMAGE_URL = "image_url";
    public final static String SHOT_ID = "shot_id";

    public ShotRecylerViewAdapter(Context context, List<Shot> myDataset) {
        mContext = context;
        mShotsList = myDataset;
    }

    @Override
    public ShotRecylerViewHolders onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shot_card_view, parent, false);

        return new ShotRecylerViewHolders(v);
    }

    @Override
    public void onBindViewHolder(ShotRecylerViewHolders holder, int position) {
        final Shot obj = mShotsList.get(position);

        User user = obj.getUser();
        Picasso.with(mContext)
                .load(user.getAvatarUrl())
                .into(holder.selfieImageView);

        holder.shotTitle.setText(obj.getTitle());
        holder.shotAuthor.setText(user.getName());



        String imageURL = getImageUrl(obj);

//        if (imageURL.endsWith(".gif")) {
//            Glide.with(mContext)
//                    .load(imageURL)
//                    .asGif()
//                    .into(holder.shotImageView);
//        }

        Picasso.with(mContext)
            .load(imageURL)
            .into(holder.shotImageView);

        holder.likeCntTextView.setText(Integer.toString(obj.getLikesCount()));
        holder.commentCntTextView.setText(Integer.toString(obj.getCommentsCount()));
        holder.viewCntTextView.setText(Integer.toString(obj.getViewsCount()));
    }

    @Override
    public int getItemCount() {
        return mShotsList.size();
    }

    private String getImageUrl(Shot user) {
        String imageURL;
        if (user.getImages().getHidpi() != null) {
            imageURL = user.getImages().getHidpi();
        }
        else if (user.getImages().getNormal() != null) {
            imageURL = user.getImages().getNormal();
        }
        else {
            imageURL = user.getImages().getTeaser();
        }
        return imageURL;
    }

    public class ShotRecylerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView selfieImageView;
        public TextView shotTitle;
        public TextView shotAuthor;
        public ImageView shotImageView;
        public TextView likeCntTextView;
        public TextView commentCntTextView;
        public TextView viewCntTextView;
        public View card;

        public ShotRecylerViewHolders(View itemView) {
            super(itemView);
            card = itemView;

            itemView.setOnClickListener(this);
            selfieImageView = (CircleImageView) itemView.findViewById(R.id.selfie_image);
            shotTitle = (TextView) itemView.findViewById(R.id.shot_title);
            shotAuthor = (TextView) itemView.findViewById(R.id.shot_author);
            shotImageView = (ImageView) itemView.findViewById(R.id.shot_image);
            likeCntTextView = (TextView) itemView.findViewById(R.id.like_cnt);
            commentCntTextView = (TextView) itemView.findViewById(R.id.comment_cnt);
            viewCntTextView = (TextView) itemView.findViewById(R.id.view_cnt);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, ShotActivity.class);
            intent.putExtra(IMAGE_URL, getImageUrl(mShotsList.get(getPosition())));
            intent.putExtra(SHOT_ID, mShotsList.get(getPosition()).getId());

            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((MainActivity) mContext, shotImageView, TRANSIT_NAME);
                mContext.startActivity(intent, options.toBundle());
            }
            else {
                mContext.startActivity(intent);
            }
        }
    }
}