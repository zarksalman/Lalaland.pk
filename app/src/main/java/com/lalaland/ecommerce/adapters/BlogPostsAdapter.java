package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.BlogPost;
import com.lalaland.ecommerce.databinding.BlogPostItemBinding;

import java.util.List;


public class BlogPostsAdapter extends RecyclerView.Adapter<BlogPostsAdapter.ProductViewHolder> {

    private Context mContext;
    private List<BlogPost> mBlogPosts;
    private BlogPostItemBinding blogPostItemBinding;
    private LayoutInflater inflater;
    private BlogPostListener mBlogPostListener;
    float width = 0;

    public BlogPostsAdapter(Context context, BlogPostListener blogPostListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mBlogPostListener = blogPostListener;
        width = getScreenWidth();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        blogPostItemBinding = DataBindingUtil.inflate(inflater, R.layout.blog_post_item, parent, false);
        blogPostItemBinding.blogParent.getLayoutParams().width = (int) (width / 1.5);
        return new ProductViewHolder(blogPostItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        BlogPost blogPost = mBlogPosts.get(position);
        holder.bindHolder(blogPost);
    }

    @Override
    public int getItemCount() {

        if (mBlogPosts != null)
            return mBlogPosts.size();

        return 0;
    }

    public void setData(List<BlogPost> blogPosts) {

        mBlogPosts = blogPosts;
        notifyDataSetChanged();
    }

    public void onBlogPostClicked(View view, BlogPost blogPost) {
        mBlogPostListener.onBlogPostClicked(blogPost);
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {

        BlogPostItemBinding mBlogPostItemBinding;

        ProductViewHolder(@NonNull BlogPostItemBinding blogPostItemBinding) {
            super(blogPostItemBinding.getRoot());

            mBlogPostItemBinding = blogPostItemBinding;
        }

        void bindHolder(BlogPost blogPost) {

            mBlogPostItemBinding.setBlogPost(blogPost);
            mBlogPostItemBinding.setAdapter(BlogPostsAdapter.this);
            mBlogPostItemBinding.executePendingBindings();
        }
    }

    public interface BlogPostListener {
        void onBlogPostClicked(BlogPost blogPost);
    }
}
