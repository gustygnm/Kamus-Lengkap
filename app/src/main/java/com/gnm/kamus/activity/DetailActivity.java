package com.gnm.kamus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gnm.kamus.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.txtKata)
    TextView txtKata;

    @BindView(R.id.txtTerjemahan)
    TextView txtTerjemahan;

    @BindView(R.id.scTampilan)
    ScrollView scTampilan;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loading.show();
        loadData();
    }

    private void loadData(){
        scTampilan.setVisibility(View.VISIBLE);
        txtKata.setText(getIntent().getStringExtra(getString(R.string.staKata)));
        txtTerjemahan.setText(getIntent().getStringExtra(getString(R.string.stTerjemahan)));
        loading.hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
