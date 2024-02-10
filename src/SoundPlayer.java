import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    private AudioInputStream audioInputStream;
    private Clip clip;

    public SoundPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.audioInputStream = AudioSystem.getAudioInputStream(new File("./assets/ticking.wav"));
        this.clip = AudioSystem.getClip();
        this.clip.open(this.audioInputStream);
    }

    public void playSound() {
        if (!clip.isRunning()) {
            clip.start();
        }
    }

    public void stopSound() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void reOpenClip() {
        stopSound();
        closeClip();
        openClip();
        playSound();
    }

    private void openClip() {
        try {
            this.clip.open(this.audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void closeClip() {
        if (clip.isOpen()) {
            clip.close();
        }
    }
}
