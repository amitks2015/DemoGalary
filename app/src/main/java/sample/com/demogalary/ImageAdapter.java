package sample.com.demogalary;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NMB384 on 9/14/2015.
 */
public class ImageAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ImageData> mImageDataList;
    LayoutInflater inflater;

    public ImageAdapter(Context context, ArrayList<ImageData> data){
        mContext = context;
        mImageDataList = data;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(mImageDataList != null) {
            return mImageDataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mImageDataList != null) {
            return mImageDataList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.locView = (TextView)view.findViewById(R.id.locString);
            holder.coView = (TextView)view.findViewById(R.id.locCoordinates);
            holder.imageView = (ImageView)view.findViewById(R.id.picView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        if(mImageDataList != null) {
            ImageData data = mImageDataList.get(i);
            holder.locView.setText(data.getlocation());
            holder.coView.setText(data.getCoordinates());
            holder.imageView.setImageBitmap(data.getImgResId());
        }
        return view;
    }

    public class ViewHolder {
        TextView locView, coView;
        ImageView imageView;
    }


}
