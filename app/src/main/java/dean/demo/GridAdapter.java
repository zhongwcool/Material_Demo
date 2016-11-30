package dean.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alex on 11/24/2016.
 */

public class GridAdapter extends BaseAdapter {
    private List<Actor> mActors;
    private Context mContext;

    public GridAdapter(Context context, List<Actor> actors) {
        this.mContext = context;
        this.mActors = actors;
    }

    @Override
    public int getCount() {
        return mActors == null?0:mActors.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mActors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;

        if(null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_view, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.pic);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Actor actor = mActors.get(position);
        mViewHolder.mTextView.setText(actor.name);
        mViewHolder.mImageView.setImageDrawable(mContext.getDrawable(actor.getImageResourceId(mContext)));

        return convertView;
    }

    private static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
