import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    private static Clip background = null;
    private static String[] playlist = {};
    private static int track = 0;
    private static float volume = 0.75f;
    private static boolean skipping = false;

    public static void setPlaylist(String[] files) {
        playlist = files;
        track = 0;
    }

    public static void playBgMusic(String filename) {
        stopBgMusic();
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(filename));
            background = AudioSystem.getClip();
            background.open(input);
            background.start();
            applyVolume(background);
            background.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && background != null && !skipping)
                    playNext();
            });
        } catch (Exception e) {
            System.out.println("BGM Error: " + e.getMessage());
        }
    }

    public static void playPlaylist(String[] files) {
        setPlaylist(files);
        if (playlist.length > 0) playBgMusic(playlist[0]);
    }

    public static void nextTrack() {
        if (playlist.length == 0) return;
        skipping = true;
        track = (track + 1) % playlist.length;
        playBgMusic(playlist[track]);
        skipping = false;
    }

    private static void playNext() {
        if (playlist.length == 0) return;
        track = (track + 1) % playlist.length;
        playBgMusic(playlist[track]);
    }

    public static void stopBgMusic() {
        if (background != null && background.isRunning()) {
            background.stop();
            background.close();
            background = null;
        }
    }

    public static void setVolume(float vol) {
        volume = Math.max(0f, Math.min(1f, vol));
        applyVolume(background);
    }

    private static void applyVolume(Clip clip) {
        if (clip == null) {
            return;
        }
        try {
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB;
            if (volume <= 0) {
                dB = fc.getMinimum();
            } else {
                dB = (float)(Math.log10(volume) * 20);
            }
            if (dB < fc.getMinimum()) dB = fc.getMinimum();
            if (dB > fc.getMaximum()) dB = fc.getMaximum();
            fc.setValue(dB);
        } catch (Exception e) {
            try {
                FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
                float vol = volume;
                if (vol < fc.getMinimum()) vol = fc.getMinimum();
                if (vol > fc.getMaximum()) vol = fc.getMaximum();
                fc.setValue(vol);
            } catch (Exception ex) {
                System.out.println("Volume not supported");
            }
        }
    }

    public static void sfx(String filename) {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(input);
            applyVolume(clip);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) clip.close();
            });
        } catch (Exception e) {
            System.out.println("SFX Error: " + e.getMessage());
        }
    }
}