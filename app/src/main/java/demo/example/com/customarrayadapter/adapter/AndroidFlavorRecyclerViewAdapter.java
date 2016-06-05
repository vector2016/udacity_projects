package demo.example.com.customarrayadapter.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.model.AndroidFlavor;

// notes: extend this class with abstract class:CursorRecyclerAdapter OR
// create another class maybe call AndroidFlavorCursorRecyclerViewAdapter
// and extend this class
public class AndroidFlavorRecyclerViewAdapter extends RecyclerView
        .Adapter<AndroidFlavorRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "AndroidFlavorRecyclerViewAdapter";
    private List<AndroidFlavor> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            //label = (TextView) itemView.findViewById(R.id.flavor_text);
            image = (ImageView) itemView.findViewById(R.id.flavor_image);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public AndroidFlavorRecyclerViewAdapter(List<AndroidFlavor> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flavor_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getVersionName());
        holder.image.setImageResource(mDataset.get(position).getImage());
    }

    public void addItem(AndroidFlavor dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}