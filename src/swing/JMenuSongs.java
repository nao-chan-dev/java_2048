package swing;

import resource.GDrive;
import resource.SongResources;

import javax.sound.sampled.LineEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JMenuSongs extends JMenu {

    private static final ExecutorService SONG_LOADER = Executors.newCachedThreadPool();
    private final List<String> SONG_QUEUE = new ArrayList<>();
    private String currentSong;

    public JMenuSongs () {
        super("SONG");
        ButtonGroup songsItemsGroup = new ButtonGroup();
        JRadioButtonMenuItem noSongItem = new JRadioButtonMenuItem("(Off)");
        noSongItem.setSelected(true);
        noSongItem.addActionListener(e -> {
            if(currentSong != null) {
                stopCurrentSong();
            }
            currentSong = null;
        });
        songsItemsGroup.add(noSongItem);
        add(noSongItem);

        Map<String, JRadioButtonMenuItem> allSongRadioItems = new HashMap<>();
        SONG_LOADER.submit(() -> {
            for (String songId : GDrive.FILE_IDS) {
                String song = SongResources.loadSong(songId);
                SONG_QUEUE.add(song);
                SongResources.SONG_CLIPS.get(song).addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP && e.getFramePosition() == SongResources.SONG_CLIPS.get(song).getFrameLength()) {
                        int nextSongIndex = SONG_QUEUE.indexOf(currentSong)+1;
                        if (nextSongIndex >= SONG_QUEUE.size()) {
                            nextSongIndex = 0;
                        }
                        String nextSong = SONG_QUEUE.get(nextSongIndex);
                        playSong(nextSong);
                        allSongRadioItems.get(nextSong).setSelected(true);
                    }
                });

                JRadioButtonMenuItem songItem = new JRadioButtonMenuItem(song);
                allSongRadioItems.put(song, songItem);
                songItem.addActionListener(e -> playSong(song));
                songsItemsGroup.add(songItem);
                add(songItem);

                if(currentSong == null) {
                    playSong(song);
                    songItem.setSelected(true);
                }
            }
        });
    }

    private void playSong(String song) {
        if(currentSong != null) {
            stopCurrentSong();
        }
        SongResources.SONG_CLIPS.get(song).start();
        currentSong = song;
    }

    private void stopCurrentSong() {
        SongResources.SONG_CLIPS.get(currentSong).stop();
        SongResources.SONG_CLIPS.get(currentSong).setMicrosecondPosition(0);
    }
}
