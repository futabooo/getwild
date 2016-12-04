package com.futabooo.getwild;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class GetWildListenerService extends WearableListenerService {

    private static final String TAG = "GetWildListenerService";

    private AudioManager audioManager;
    private int originalVol;
    private int maxVol;

    private TextToSpeech tts;


    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d(TAG, "tts onInit");
            }
        });
    }

    @Override
    public void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVol, 0);
        super.onDestroy();
    }

    @Override
    public void onPeerDisconnected(Node node) {
        super.onPeerDisconnected(node);
        Log.d(TAG, "onPeerDisconnected");
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
        startSing();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "onMessageReceived");
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, 0);
        startSing();
    }

    private void startSing() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sp.getBoolean("isEnable", false)) {
            return;
        }

        getWildAndTough(Locale.ENGLISH, getString(R.string.lyrics_get_wild_and_tough));
        getWildAndTough(Locale.JAPAN, getString(R.string.lyrics_1));
        getWildAndTough(Locale.ENGLISH, getString(R.string.lyrics_get_wild_and_tough));
        getWildAndTough(Locale.JAPAN, getString(R.string.lyrics_2));
        getWildAndTough(Locale.ENGLISH, getString(R.string.lyrics_get_chance_and_luck));
        getWildAndTough(Locale.JAPAN, getString(R.string.lyrics_3));
        getWildAndTough(Locale.ENGLISH, getString(R.string.lyrics_get_chance_and_luck));
        getWildAndTough(Locale.JAPAN, getString(R.string.lyrics_4));
    }

    private void getWildAndTough(Locale locale, String string) {
        if (locale == Locale.JAPAN) {
            tts.setSpeechRate(2.0f);
        } else if (locale == Locale.ENGLISH) {
            tts.setSpeechRate(1.0f);
        }

        tts.setLanguage(locale);
        tts.speak(string, TextToSpeech.QUEUE_ADD, null, "1");
    }
}
