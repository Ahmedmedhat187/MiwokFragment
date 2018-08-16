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

public class FragmentNumbers extends Fragment {


    WordAdapter numbersAdapter;
    ArrayList<WordObject> numbers;
    ListView listNumbers;

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


    public FragmentNumbers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        numbers = new ArrayList<>();
        numbers.add(new WordObject("One"   , "Lutti"    , R.drawable.number_one   , R.raw.number_one) );
        numbers.add(new WordObject("Two"   , "Otiiko"   , R.drawable.number_two   , R.raw.number_two));
        numbers.add(new WordObject("Three" , "Tolookosu", R.drawable.number_three , R.raw.number_three));
        numbers.add(new WordObject("Four"  , "Oyyisa"   , R.drawable.number_four  , R.raw.number_four));
        numbers.add(new WordObject("Five"  , "Massokka" , R.drawable.number_five  , R.raw.number_five));
        numbers.add(new WordObject("Six"   , "Temmokka" , R.drawable.number_six   , R.raw.number_six));
        numbers.add(new WordObject("Seven" , "Kenekaku" , R.drawable.number_seven , R.raw.number_seven));
        numbers.add(new WordObject("Eight" , "Kawinta"  , R.drawable.number_eight , R.raw.number_eight));
        numbers.add(new WordObject("Nine"  , "Wo’e"     , R.drawable.number_nine  , R.raw.number_nine));
        numbers.add(new WordObject("Ten"   , "Na’aacha" , R.drawable.number_ten   , R.raw.number_ten));

        numbersAdapter = new WordAdapter(getActivity() , numbers , R.color.category_numbers );
        listNumbers = (ListView)v.findViewById(R.id.list);
        listNumbers.setAdapter(numbersAdapter);

        listNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //   Toast.makeText(NumbersActivity.this, "item click " + position, Toast.LENGTH_SHORT).show();

                WordObject numberObject = numbers.get(position);

                releaseMediaPlayer();

                int result = audioManager.requestAudioFocus( onAudioFocusChangeListener ,AudioManager.STREAM_MUSIC ,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    mediaPlayer = MediaPlayer.create(getActivity() , numberObject.getSound());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }

            }});

        return v;
    }



    @Override
    public void onStop(){
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
