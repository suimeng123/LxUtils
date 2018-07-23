package com.lx.utils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.lx.utils.R;
import com.lx.utils.view.LxMusicPlayerView;

import java.io.IOException;

/**
 * Created by lixiao2 on 2018/6/21.
 */

public class MusicPlayerActivity extends Activity{

    LxMusicPlayerView lxMusicPlayerView;
    SeekBar seekBar ;
    private boolean isPlay = true;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        lxMusicPlayerView = findViewById(R.id.music_pv);
        seekBar = findViewById(R.id.seekBar);
        btn = findViewById(R.id.btn);

        try {
            lxMusicPlayerView.setFileInfo(getAssets().open("超级英雄.lrc"),getAssets().open("超级英雄.mp3"),"超级英雄.mp3");
            lxMusicPlayerView.setSeekBar(seekBar);
            lxMusicPlayerView.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlay){
                    boolean s = lxMusicPlayerView.pause();
                    if(s) {
                        isPlay = false;
                        btn.setText("播放");
                    }
                }else {
                    boolean s = lxMusicPlayerView.reStart();
                    if(s){
                        isPlay = true;
                        btn.setText("暂停");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(lxMusicPlayerView != null) {
            lxMusicPlayerView.onDestory();
        }
    }
}
