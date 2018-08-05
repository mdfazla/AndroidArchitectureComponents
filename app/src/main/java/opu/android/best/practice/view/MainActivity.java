package opu.android.best.practice.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;

import opu.android.best.practice.R;
import opu.android.best.practice.model.AdapterModel;
import opu.android.best.practice.presenter.ImageLoadingContract;
import opu.android.best.practice.presenter.Presenter;
import opu.android.best.practice.utils.CustomGridLayoutManager;


public class MainActivity extends AppCompatActivity implements ImageLoadingContract.View {

    private Presenter presenter;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_base);
        initRecyclerView();

        getImages();
    }

    private void initRecyclerView() {
        toolbar=findViewById(R.id.toolbar);
        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        adapter.addItems(images);
    }

    @Override
    public void onError(String ex) {

    }
}
