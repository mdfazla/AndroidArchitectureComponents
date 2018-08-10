package opu.android.best.practice.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;


import opu.android.best.practice.R;
import opu.android.best.practice.model.AdapterModel;
import opu.android.best.practice.model.DataModel;
import opu.android.best.practice.model.HeaderModel;
import opu.android.best.practice.model.ImageInfo;
import opu.android.best.practice.model.ImagesViewModel;
import opu.android.best.practice.model.TitleModel;
import opu.android.best.practice.presenter.ImageLoadingContract;
import opu.android.best.practice.presenter.Presenter;
import opu.android.best.practice.room.ArchComponentDatabase;
import opu.android.best.practice.room.entity.ImageEntity;
import opu.android.best.practice.utils.Constant;
import opu.android.best.practice.utils.CustomGridLayoutManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements ImageLoadingContract.View {

    private Presenter presenter;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private Toolbar toolbar;
    private ArrayList<AdapterModel> adapterModels;
    private ImagesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.hideStatusBar(getWindow());
        setContentView(R.layout.activity_recycle_view_base);
        viewModel = ViewModelProviders.of(this).get(ImagesViewModel.class);

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
        if (presenter != null) {
            presenter.dispose();
            presenter = null;
        }
        adapter = null;
        adapterModels = null;
        toolbar = null;
        recyclerView = null;
        viewModel = null;
        super.onDestroy();
    }

    private void getImages() {

        if (viewModel.getImageList() == null || viewModel.getImageList().size() == 0) {
            if(adapterModels==null)
                adapterModels=new ArrayList<>();
            HandlerThread thread = new HandlerThread("databaseQuery");
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ArchComponentDatabase database = ArchComponentDatabase.getDatabase(MainActivity.this);
                    List<ImageEntity> list = database.getImgDAO().getImages();

                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            ImageEntity entity = list.get(i);
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setImgName(entity.getName());
                            imageInfo.setImgUrl(entity.getImgUrl());
                            imageInfo.setId(entity.getId());
                            if (imageInfo.getImgName().equals("Header")) {
                                HeaderModel headerModel = new HeaderModel();
                                headerModel.setData(imageInfo);
                                adapterModels.add(0, headerModel);
                                adapterModels.add(1, new TitleModel());
                            } else {
                                DataModel dataModel = new DataModel();
                                dataModel.setId(imageInfo.getId());
                                dataModel.setImgName(imageInfo.getImgName());
                                dataModel.setImgUrl(imageInfo.getImgUrl());
                                adapterModels.add(dataModel);

                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addItems(adapterModels);
                            }
                        });

                    } else {
                        presenter = new Presenter(MainActivity.this);
                        presenter.loadImages();
                    }
                }
            });


        } else {
            adapterModels = viewModel.getImageList();
            adapter.addItems(adapterModels);
        }

    }

    @Override
    public void onLoadImages(ArrayList<AdapterModel> images) {
        adapterModels = images;
        viewModel.setImageList(images);
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
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
}
