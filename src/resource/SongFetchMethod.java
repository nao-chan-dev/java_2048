package resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public enum SongFetchMethod {

    PUBLIC( songId -> {
        try {
            URL songUrl = new URL(GDrive.PUBLIC_URL + songId);
            String header = songUrl.openConnection().getHeaderField("content-disposition");
            return header.substring(header.indexOf("filename=\"") + "filename=\"".length(), header.indexOf("."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, songId -> {
        try {
            return new BufferedInputStream(new URL(GDrive.PUBLIC_URL + songId).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }),
    API_KEY( songId -> {
        try {
            URL songNameUrl = new URL(GDrive.API_URL + songId + "?key="+GDrive.KEY+"&fields=name");
            String response = new String(songNameUrl.openStream().readAllBytes(), StandardCharsets.UTF_8);
            return response.substring(response.indexOf("\"name\": \"")+"\"name\": \"".length(), response.indexOf("."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, songId -> {
        try {
            return new BufferedInputStream(new URL(GDrive.API_URL + songId + "?key="+GDrive.KEY+"&alt=media").openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }),
    SERVICE_ACCOUNT( songId -> {
        try {
            URL songNameUrl = new URL(GDrive.API_URL + songId + "?fields=name");
            URLConnection connection = songNameUrl.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + GDrive.TOKEN);
            String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return response.substring(response.indexOf("\"name\": \"")+"\"name\": \"".length(), response.indexOf("."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, songId -> {
        try {
            URL songUrl = new URL(GDrive.API_URL + songId + "?alt=media");
            URLConnection connection = songUrl.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + GDrive.TOKEN);
            return new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    SongFetchMethod(Function<String, String> songNameMethod, Function<String, InputStream> songStreamMethod) {
        this.songNameMethod = songNameMethod;
        this.songStreamMethod = songStreamMethod;
    }

    private final Function<String, String> songNameMethod;
    private final Function<String, InputStream> songStreamMethod;

    public String fetchSongName(String songId) {
        return songNameMethod.apply(songId);
    }

    public InputStream fetchSong(String songId) {
        return songStreamMethod.apply(songId);
    }
}
