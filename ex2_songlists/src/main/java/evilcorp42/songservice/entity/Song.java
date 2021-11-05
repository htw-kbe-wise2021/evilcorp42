package evilcorp42.songservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import evilcorp42.songservice.controller.SongController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
public class Song {

    private static final Logger log = LoggerFactory.getLogger(SongController.class);

    private @Id
    int id;
    private String title;
    private String artist;
    private String label;
    private int released;

    private Song(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.artist = builder.artist;
        this.label = builder.label;
        this.released = builder.released;
    }

    public Song() {
    }

    public Song(int id
            , String title
            , String artist
            , String label
            , int released) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.label = label;
        this.released = released;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    /**
     * Wandelt das Objekt in ein JSON-String um
     * @return Objekt im JSON-Format
     */
    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            System.out.println("ResultingJSONstring = " + json);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wandelt einen JSON-String in ein Song-Objekt um
     * ACHTUNG: Falls eine ID vorhanden ist wird diese hier entfernt
     * @param json - String mit dem Object in JSON-Format
     * @return Song-Objekt wenn der JSON dafür geeignet ist, null wenn es einen Fehler gibt
     */
    public static Song jsonToSong(String json) {
        //TODO: Methode(jsonToSong) muss noch getestet werden
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Song.class);
        } catch (JsonProcessingException e) {
            log.info("jsonToSong() konnte folgenden String nicht in ein Song umwandeln: " + System.getProperty("line.separator") + json);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wandelt das Objekt in ein XML-String um
     * @return Objekt im XML-Format
     */
    public String toXML(){
        XmlMapper mapper = new XmlMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.info("toXML() konnte das Objekt nicht in ein XML-Format umwandeln.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wandelt den String in ein Song-Objekt um
     * @param xml_String ist ein String mit einem Objekt in XML-Format
     * @return Song-Objekt wenn der JSON dafür geeignet ist, null wenn es einen Fehler gibt
     */
    public static Song xmlToSong(String xml_String) {
        //TODO: Methode(xmlToSong) muss noch implementiert werden
        log.error("xmlToSong( ist in Song nocht nicht implementiert.)");
        return null;
    }


    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", label='" + label + '\'' +
                ", released=" + released +
                '}';
    }

    public static Song.Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer id;
        private String title;
        private String artist;
        private String label;
        private int released;

        private Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withReleased(int released) {
            this.released = released;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }
}
