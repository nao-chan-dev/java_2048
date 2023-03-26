package resource;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SongResources {

    private static final SongFetchMethod FETCH_MODE = SongFetchMethod.SERVICE_ACCOUNT;
    private static final boolean CACHING_ENABLED = true;
    private static final String TEMP_FOLDER_PATH = System.getProperty("java.io.tmpdir") + File.separator + "Nao2048" + File.separator;

    public static final Map<String, Clip> SONG_CLIPS = new HashMap<>();

    public static String loadSong(String songId) {
        try {
            String songName;
            AudioInputStream audioStream;
            Clip songClip = AudioSystem.getClip();
            File cachedFilePath = new File(TEMP_FOLDER_PATH + songId);
            File[] cachedFileDirectory = cachedFilePath.listFiles();

            if(cachedFilePath.exists() && Objects.requireNonNull(cachedFileDirectory).length > 0 && cachedFileDirectory[0].isFile()) {
                if(cachedFileDirectory[0].length() == 0) {
                    cachedFileDirectory[0].delete();
                    loadSong(songId);
                }
                audioStream = AudioSystem.getAudioInputStream(cachedFileDirectory[0]);
                songName = cachedFileDirectory[0].getName().split("\\.")[0];
            }
            else
            {
                songName = FETCH_MODE.fetchSongName(songId);
                audioStream = AudioSystem.getAudioInputStream(FETCH_MODE.fetchSong(songId));
                if(CACHING_ENABLED) {
                    File cachedFile = new File(TEMP_FOLDER_PATH + songId + File.separator + songName + ".wav");
                    cachedFile.getParentFile().mkdirs();
                    cachedFile.createNewFile();
                    AudioSystem.write(new AudioInputStream(
                            new ByteArrayInputStream(audioStream.readAllBytes()),
                            audioStream.getFormat(),
                            audioStream.getFrameLength()), AudioFileFormat.Type.WAVE, cachedFile);
                    audioStream = AudioSystem.getAudioInputStream(cachedFile);
                }
            }

            songClip.open(audioStream);
            ((FloatControl) songClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-10);
            SONG_CLIPS.put(songName, songClip);
            return songName;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
