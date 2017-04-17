package com.fly.flyman3046.dribbbleclient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fly.flyman3046.dribbbleclient.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecylerViewAdapter extends RecyclerView.Adapter<CommentRecylerViewAdapter.CommentRecylerViewAdapterHolder> {
    private final static String TAG = CommentRecylerViewAdapter.class.getSimpleName();
    private List<Comment> mCommentList;
    private Context mContext;

    public CommentRecylerViewAdapter(Context context, List<Comment> myDataset) {
        mContext = context;
        mCommentList = myDataset;
    }

    @Override
    public CommentRecylerViewAdapterHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card_view, parent, false);

        return new CommentRecylerViewAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentRecylerViewAdapterHolder holder, int position) {
        final Comment obj = mCommentList.get(position);

        String comm = obj.getBody();
        comm = comm.replace("<p>", "");
        comm = comm.replace("</p>", "");
        holder.commentTextView.setText(Html.fromHtml(comm));

        Picasso.with(mContext)
                .load(obj.getUser().getAvatarUrl())
                .resize(100, 100)
                .into(holder.selfieImageView);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public class CommentRecylerViewAdapterHolder extends RecyclerView.ViewHolder {
        public CircleImageView selfieImageView;
        public TextView commentTextView;
        public View card;

        public CommentRecylerViewAdapterHolder(View itemView) {
            super(itemView);
            card = itemView;

            selfieImageView = (CircleImageView) itemView.findViewById(R.id.selfie_image);
            commentTextView = (TextView) itemView.findViewById(R.id.comment_content);
        }
    }
}