package dean.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dean Guo on 10/20/14.
 */
class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{

    private List<Actor> mActors;
    private Context mContext;

    ListAdapter(Context context, List<Actor> actors) {
        this.mContext = context;
        this.mActors = actors;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int position ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int position ) {
        Actor p = mActors.get(position);
        viewHolder.mContext = mContext;
        viewHolder.mTextView.setText(p.name);
        viewHolder.mImageView.setImageDrawable(mContext.getDrawable(p.getImageResourceId(mContext)));
    }

    @Override
    public int getItemCount()
    {
        return mActors == null ? 0 : mActors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ImageView mImageView;

        Context mContext;

        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            mImageView = (ImageView) view.findViewById(R.id.pic);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                ((MainActivity)mContext).startActivity(v, getPosition());
                }
            });
        }
    }
}
