import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SoundPlayer {

    private List<Clip> clipList = new ArrayList<Clip>();
    private int currentSoundPlayedIndex = 0;
    public SoundPlayer(){

    }

    public SoundPlayer(String soundFileName)
    {
        try
        {
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clipList.set(0, AudioSystem.getClip());
            clipList.get(0).open(sound);

        }catch (Exception e)
        {
            throw new RuntimeException("Nie udalo sie utworzyc odtwarzacza");
        }
    }

    public void setSound(String soundFileName){
        try
        {
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clipList.set(0,AudioSystem.getClip());
            clipList.get(0).open(sound);

        }catch (Exception e)
        {
            throw new RuntimeException("Nie udalo sie ustawic dzwieku");
        }
    }


    public void setSound(URI soundFileUri){
        try
        {
            File file = new File(soundFileUri);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clipList.set(0,AudioSystem.getClip());
            clipList.get(0).open(sound);

        }catch (Exception e)
        {
            throw new RuntimeException("Nie udalo sie ustawic dzwieku");
        }
    }

    public void playFirstSound()
    {
        clipList.get(0).setFramePosition(0);
        clipList.get(0).start();
    }

    public void playSoundWithEndListener(){

        clipList.get(0).addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                ImagePanel.setIsSoundsPlayed(true);
            }
        });

        clipList.get(0).start();

    }

    public void setSoundList(List<URI> soundFileUriList) {


        for (int i = 0; i < soundFileUriList.size(); i++) {
            try {
//                clipList = new ArrayList<Clip>(soundFileUriList.size());
                File file = new File(soundFileUriList.get(i));
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clipList.add(AudioSystem.getClip());
//                clipList.set(i, AudioSystem.getClip());
                clipList.get(i).open(sound);

                clipList.get(i).addLineListener(e -> {
//                    if (e.getType() == LineEvent.Type.STOP && currentSoundPlayedIndex {
                    if (e.getType() == LineEvent.Type.STOP) {
                        playNextSound();
//                        lastplayedCounter
                    }
                });

            } catch (Exception e) {
                throw new RuntimeException("Nie udalo sie ustawic dzwieku");
            }
        }

    }

    public void playSoundList(){
            playNextSound();
        }

        public void playNextSound(){
        if(currentSoundPlayedIndex < clipList.size()){
            Clip clip = clipList.get(currentSoundPlayedIndex);
            clip.start();
            currentSoundPlayedIndex++;
        }
        else {
            ImagePanel.setIsSoundsPlayed(true);
            currentSoundPlayedIndex = 0;
            for (Clip clip :clipList) {
                clip.setMicrosecondPosition(0);
            }
        }
    }
}