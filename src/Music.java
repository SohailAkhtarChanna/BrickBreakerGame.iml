import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Music {

    Clip clip; //it will hold the audio clip
    URL soundUrl[] = new URL[2]; //URL for storing sound path

    public Music(){
        soundUrl[0] = getClass().getResource("BeepBox-Song.wav"); //get the file path and store in each url
    }

    public void setFile(int i){ //first step is to open an audio file in java

        try{
            AudioInputStream audios = AudioSystem.getAudioInputStream(soundUrl[i]); //load each audio file into the clip
            clip = AudioSystem.getClip();
            clip.open(audios);
        }
        catch (Exception e){}

    }
    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }

}
