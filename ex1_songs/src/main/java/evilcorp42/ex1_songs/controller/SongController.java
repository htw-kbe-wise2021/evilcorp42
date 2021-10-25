package evilcorp42.ex1_songs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import evilcorp42.ex1_songs.entity.Song;
import evilcorp42.ex1_songs.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/songs")
public class SongController {

    //Diese Zeile dient nur für eine besser Ausgabe in der Konsole
    private static final Logger log = LoggerFactory.getLogger(SongController.class);

    private final SongRepository songRepository; // SpringBoot-Magic

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository; // SpringBoot-Magic
    }


    /**
     * Funktion gibt sämtliche Songs zurueck
     */
    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> getAllSongs(@RequestHeader("Accept") String accept) {
        System.out.println("getAllSongs() wird ausgefuehrt.");

        List<Song> liste = songRepository.findAll();//SpringBoot-Magic

        try {
            String ausgabe = "";
            if (accept.equals(MediaType.APPLICATION_JSON_VALUE)
                    || accept.equals(MediaType.ALL_VALUE)) { // Ausgabeformat ist JSON
                ausgabe = new ObjectMapper().writeValueAsString(liste);
            }
            log.info("GET-Request: song(All).count= " + liste.size()); // Konsolenausgabe
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(ausgabe, header, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Fehler in getSongs() bei der Vergabe aller Song");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
