import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer {

    private Clip clip;

    public SoundPlayer(String soundFileName)
    {
        try
        {
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }

    public void playSound()
    {
        clip.setFramePosition(0);
        clip.start();
    }
}