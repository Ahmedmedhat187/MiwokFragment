package com.example.android.miwokfragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentFamily extends Fragment {

    WordAdapter familyAdapter;
    ArrayList<WordObject> family;

    ListView listFamily;

    MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }};

    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
        }};



    public FragmentFamily() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        family = new ArrayList<>();
        family.add(new WordObject("father"         , "әpә"     , R.drawable.family_father          , R.raw.family_father));
        family.add(new WordObject("mother"         , "әṭa"      , R.drawable.family_mother         , R.raw.family_mother));
        family.add(new WordObject("son"            , "angsi"   , R.drawable.family_son             , R.raw.family_son));
        family.add(new WordObject("daughter"       , "tune"    , R.drawable.family_daughter        , R.raw.family_daughter));
        family.add(new WordObject("older brother"  , "taachi"  , R.drawable.family_older_brother   , R.raw.family_older_brother));
        family.add(new WordObject("younger brother", "chalitti", R.drawable.family_younger_brother , R.raw.family_younger_brother));
        family.add(new WordObject("older sister"   , "teṭe"    , R.drawable.family_older_sister     , R.raw.family_older_sister));
        family.add(new WordObject("younger sister" , "kolliti" , R.drawable.family_younger_sister  , R.raw.family_younger_sister));
        family.add(new WordObject("grandmother "   , "ama"     , R.drawable.family_grandmother     , R.raw.family_grandmother));
        family.add(new WordObject("grandfather"    , "paapa"   , R.drawable.family_grandfather     , R.raw.family_grandfather));

        familyAdapter = new WordAdapter(getActivity() , family, R.color.category_family  );
        listFamily = (ListView) v.findViewById(R.id.list);
        listFamily.setAdapter(familyAdapter);

        listFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WordObject familyObject = family.get(position);

                releaseMediaPlayer();

                int result = audioManager.requestAudioFocus( onAudioFocusChangeListener ,AudioManager.STREAM_MUSIC ,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mediaPlayer = MediaPlayer.create(getActivity(), familyObject.getSound());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }});

        return v;
    }


    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }


}
