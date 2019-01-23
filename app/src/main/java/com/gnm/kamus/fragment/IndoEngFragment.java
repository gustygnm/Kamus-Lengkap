package com.gnm.kamus.fragment;

import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.gnm.kamus.R;
import com.gnm.kamus.adapter.AdapterKamus;
import com.gnm.kamus.model.Kamus;
import com.gnm.kamus.database.KamusHelper;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndoEngFragment extends Fragment implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.txtSearch)
    SearchView txtSearch;

    @BindView(R.id.imgSpeak)
    ImageView imgSpeak;

    private KamusHelper kamusHelper;
    private AdapterKamus adapter;

    private ArrayList<Kamus> list = new ArrayList<>();
    private boolean isEnglish = false;
    private TextToSpeech mTTS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kamus, container, false);

        ButterKnife.bind(this, view);

        kamusHelper = new KamusHelper(this.getContext());

        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                loadData(s);
                return false;
            }
        });
        setupList();
        loadData();


        mTTS = new TextToSpeech(this.getContext(), this);
        mTTS.setLanguage(new Locale("id","ID"));

        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(txtSearch.getQuery().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private void setupList() {
        adapter = new AdapterKamus();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadData(String search) {
        try {
            kamusHelper.open();
            if (search.isEmpty()) {
                list = kamusHelper.getAllData(isEnglish);
            } else {
                list = kamusHelper.getDataByName(search, isEnglish);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
            loading.hide();
        }
        adapter.replaceAll(list);
    }

    private void loadData() {
        loadData("");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(new Locale("id","ID"));

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            } else {
                mTTS.setOnUtteranceCompletedListener(this);
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }
}
