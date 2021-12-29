import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.File;


public class SoundEffect{

	public Clip clip;
	public boolean isRunning = false;

	//Methode pour charger le fichier audio
	public void setFile(String fileName){   
		try{
			File file = new File(fileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sound);
		}catch(Exception e){

		}
	}
	
	//On lance le fichier audio
	public void play(){
		clip.setFramePosition(0);
		clip.start();
		isRunning = true;
		
	}

	//On arrete le fichier audio
	public void stop(){
		clip.stop();
		isRunning = false;
	}

	//Permet de joueur le fichier audio en boucle
	public void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
}