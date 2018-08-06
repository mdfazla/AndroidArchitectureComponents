package opu.android.best.practice.view;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import java.util.ArrayList;

import opu.android.best.practice.R;
import opu.android.best.practice.model.AdapterModel;
import opu.android.best.practice.model.DataModel;
import opu.android.best.practice.model.HeaderModel;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.presenter.ImageLoadingContract;
import opu.android.best.practice.presenter.Presenter;
import opu.android.best.practice.utils.Constant;
import opu.android.best.practice.utils.CustomGridLayoutManager;


public class MainActivity extends AppCompatActivity implements ImageLoadingContract.View {

    private Presenter presenter;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private Toolbar toolbar;
    private ArrayList<AdapterModel> adapterModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.hideStatusBar(getWindow());
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recycle_view_base);
        initRecyclerView();

        getImages();
    }

    private void initRecyclerView() {
        toolbar = findViewById(R.id.toolbar);
        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView slideShow = findViewById(R.id.slide_show);
        slideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getImageInfo().size() > 0)
                    SlideShowFragment.showDialog(getImageInfo(), MainActivity.this).show(getSupportFragmentManager(), SlideShowFragment.class.getName());
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        final GridLayoutManager layoutManager = new CustomGridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case AdapterModel.HEADER:
                    case AdapterModel.TITLE:
                        return 2;
                    case AdapterModel.ITEM:
                        return 1;
                    default:
                        return 1;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    View view = layoutManager.findViewByPosition(0);
                    view.setTranslationY(-view.getTop() / 2);
                    toolbar.setTranslationY(view.getTop());
                }
            }
        });

    }

    @Override
    protected void onDestroy() {

        presenter.dispose();
        presenter = null;
        super.onDestroy();
    }

    private void getImages() {
        presenter = new Presenter(this);
        presenter.loadImages();

    }

    @Override
    public void onLoadImages(ArrayList<AdapterModel> images) {
        adapterModels = images;
        adapter.addItems(images);
    }

    private ArrayList<ImageInfo> getImageInfo() {
        ArrayList<ImageInfo> list = new ArrayList<>();
        if (adapterModels != null) {
            for (AdapterModel model : adapterModels) {
                if (model.getType() == AdapterModel.ITEM || model.getType() == AdapterModel.HEADER) {
                    if (model.getType() == AdapterModel.HEADER) {
                        list.add(((HeaderModel) model).getData());
                    } else
                        list.add((DataModel) model);
                }
            }
        }
        return list;
    }

    @Override
    public void onError(String ex) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
}
