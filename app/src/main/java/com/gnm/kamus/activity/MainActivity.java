package com.gnm.kamus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gnm.kamus.R;
import com.gnm.kamus.adapter.AdapterViewpager;
import com.gnm.kamus.fragment.EngIndoFragment;
import com.gnm.kamus.fragment.IndoEngFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab)
    TabLayout tab;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initViewpager(viewPager);
        tab.setupWithViewPager(viewPager);
    }

    void initViewpager(ViewPager viewPager) {
        AdapterViewpager adapter = new AdapterViewpager(getSupportFragmentManager());
        adapter.getFragment(new IndoEngFragment(), getString(R.string.stIdEn));
        adapter.getFragment(new EngIndoFragment(), getString(R.string.stEnId));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_setting) {
            Intent mIntent = new Intent(this, SettingActivity.class);
            startActivity(mIntent);
            return true;
        } else if (id == R.id.menu_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Aplikasi Kamus Lengkap.\nSilahkan download melalui link berikut: https://play.google.com/store/apps/details?id=com.gnm.kamus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        } else if (id == R.id.menu_tentang) {
            Intent mIntent = new Intent(this, AboutActivity.class);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    boolean doubleBackToExit = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity();
        } else {
            Toast.makeText(this, getString(R.string.message_exit), Toast.LENGTH_SHORT).show();
        }
        int timee = 2000;
        this.doubleBackToExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, timee);
    }
}
