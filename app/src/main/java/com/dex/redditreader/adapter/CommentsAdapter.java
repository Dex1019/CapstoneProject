package com.dex.redditreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dex.redditreader.R;
import com.dex.redditreader.utils.DateTimeUtil;
import com.google.common.collect.FluentIterable;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.CommentNode;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsAdapterViewHolder> {
    private Context context;
    private FluentIterable<CommentNode> commentNodes;

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommentsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_list, parent, false);
        return new CommentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsAdapterViewHolder holder, int position) {
        CommentNode commentNode = commentNodes.get(position);
        Comment comment = commentNode.getComment();
        holder.mBody.setText(comment.getBody());
        holder.mAuthor.setText(context.getString(R.string.username_prefix) + comment.getAuthor() + "   "
                + DateTimeUtil.convert(comment.getCreated().getTime()));
        holder.itemView.setPadding(makeIndent(commentNode.getDepth()), 0, 0, 0);
    }

    public void setCommentNodes(FluentIterable<CommentNode> commentNodes) {
        this.commentNodes = commentNodes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (commentNodes == null)
            return 0;
        return commentNodes.size();
    }

    private int makeIndent(int depth) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (10 * depth * scale + 0.5f);
    }

    public class CommentsAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.body)
        TextView mBody;
        @BindView(R.id.author)
        TextView mAuthor;

        public CommentsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}