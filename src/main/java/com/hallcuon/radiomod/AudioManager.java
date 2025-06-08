package com.hallcuon.radiomod;

public class AudioManager {
    private static AudioManager instance;
    private final AudioPlayer player = new AudioPlayer();
    private boolean isPlaying = false;

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    public void play(String url) {
        if (!isPlaying) {
            try {
                player.playFromYouTube(url);
                isPlaying = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (isPlaying) {
            player.stopCurrentPlayback();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
