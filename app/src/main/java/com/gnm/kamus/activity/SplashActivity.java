package com.gnm.kamus.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gnm.kamus.R;
import com.gnm.kamus.pref.AppPreference;
import com.gnm.kamus.model.Kamus;
import com.gnm.kamus.database.KamusHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.txtLoad)
    TextView tv_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {
        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(SplashActivity.this);
            appPreference = new AppPreference(SplashActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                tv_load.setText(getString(R.string.memuad_data));
                ArrayList<Kamus> kamusEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<Kamus> kamusIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kamusHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
                tv_load.setText(getString(R.string.mohon_tunggu));
                try {
                    synchronized (this) {
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<Kamus> preLoadRaw(int data) {
        ArrayList<Kamus> kamusModels = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                Kamus kamusModel;
                kamusModel = new Kamus(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusModels;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
