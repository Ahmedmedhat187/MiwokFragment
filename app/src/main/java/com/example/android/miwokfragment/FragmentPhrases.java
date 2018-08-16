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

public class FragmentPhrases extends Fragment {

    WordAdapter phrasesAdapter;
    ArrayList<WordObject> phrases;
    ListView listPhrases;

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




    public FragmentPhrases() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);


        phrases = new ArrayList<>();
        phrases.add(new WordObject("Where are you going?", "minto wuksus"    , R.raw.phrase_where_are_you_going  ));
        phrases.add(new WordObject("What is your name?"  , "tinnә oyaase'nә" , R.raw.phrase_what_is_your_name  ));
        phrases.add(new WordObject("My name is..."       , "oyaaset..."      , R.raw.phrase_my_name_is));
        phrases.add(new WordObject("How are you feeling?", "michәksәs?"      , R.raw.phrase_how_are_you_feeling ));
        phrases.add(new WordObject("I’m feeling good."   , "kuchi achit"     , R.raw.phrase_im_feeling_good));
        phrases.add(new WordObject("Are you coming?"     , "әәnәs'aa?"       , R.raw.phrase_are_you_coming));
        phrases.add(new WordObject("Yes, I’m coming."    , "hәә’ әәnәm"      , R.raw.phrase_yes_im_coming));
        phrases.add(new WordObject("I’m coming."         , "әәnәm"           , R.raw.phrase_im_coming));
        phrases.add(new WordObject("Let’s go."           , "yoowutis"        , R.raw.phrase_lets_go));
        phrases.add(new WordObject("Come here."          , "әnni'nem"        , R.raw.phrase_come_here));
        phrases.add(new WordObject("My name is..."       , "oyaaset..."      , R.raw.phrase_my_name_is));


        phrasesAdapter = new WordAdapter(getActivity() , phrases , R.color.category_phrases );
        listPhrases = (ListView) v.findViewById(R.id.list);
        listPhrases.setAdapter(phrasesAdapter);

        listPhrases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                WordObject phrasesObject = phrases.get(position);

                releaseMediaPlayer();

                int result = audioManager.requestAudioFocus( onAudioFocusChangeListener ,AudioManager.STREAM_MUSIC ,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mediaPlayer = MediaPlayer.create(getActivity(), phrasesObject.getSound());
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
