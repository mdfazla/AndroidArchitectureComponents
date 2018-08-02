package sharemythoughts.android.com.recycleviewwithvariantgrid.view;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;

import sharemythoughts.android.com.recycleviewwithvariantgrid.R;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.AdapterModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.DataModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.model.HeaderModel;
import sharemythoughts.android.com.recycleviewwithvariantgrid.utils.Constant;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<AdapterModel> list;
    private int width, height, cellWidth;
    private AppCompatActivity activity;


    public ImageAdapter(AppCompatActivity activity) {
        list = new ArrayList<>();
        this.activity = activity;
        int[] res = Constant.calculateDeviceResolution(activity);
        height = res[0];
        width = res[1];

    }


    public void addItems(ArrayList<AdapterModel> items) {
        if (list.isEmpty()) {
            list.addAll(items);
            notifyDataSetChanged();
        } else {
            int pos = list.size();
            list.addAll(items);
            notifyItemRangeInserted(pos, items.size());
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AdapterModel.HEADER:
                View headerView = LayoutInflater.from(activity).inflate(R.layout.header_layout, parent, false);
                return new HeaderViewHolder(headerView);
            case AdapterModel.ITEM:
                View itemView = LayoutInflater.from(activity).inflate(R.layout.single_item, parent, false);
                return new DataViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int viewType = list.get(position).getType();
        switch (viewType) {
            case AdapterModel.HEADER:
                HeaderViewHolder headerModel = (HeaderViewHolder) holder;
                headerModel.loadData((HeaderModel) list.get(position));
                break;

            case AdapterModel.ITEM:
                DataViewHolder dataViewHolder = (DataViewHolder) holder;
                dataViewHolder.loadData((DataModel) list.get(position));
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public HeaderViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.header_img);
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = width;
        }

        public void loadData(HeaderModel model) {
            Constant.loadGif(imageView.getContext(), model.getData().getImgUrl(), imageView);
        }
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public DataViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_img);
            if (cellWidth == 0)
                calculateCellWidth(view);
            else {
                //imageView.getLayoutParams().width = cellWidth;
                imageView.getLayoutParams().height = cellWidth;
            }
        }

        private void calculateCellWidth(final View vIew) {
            vIew.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            cellWidth = vIew.getWidth();
                            imageView.getLayoutParams().height = cellWidth;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                vIew.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                vIew.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        }
                    });

        }

        public void loadData(DataModel model) {
            Constant.setImage(imageView.getContext(), model.getImgUrl(), imageView);
        }
    }
}
