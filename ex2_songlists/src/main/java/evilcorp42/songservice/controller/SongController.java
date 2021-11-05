package evilcorp42.songservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import evilcorp42.songservice.entity.Song;
import evilcorp42.songservice.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController //Controller nach dem REST-Prinzip
@RequestMapping("/evilcorp42/songs")
public class SongController {

    //Diese Zeile dient nur für eine besser Ausgabe in der Konsole
    private static final Logger log = LoggerFactory.getLogger(SongController.class);

    private final SongRepository songRepository;

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /*
     * reponse zur aufgabe 4
     */
    @GetMapping(
            value="/{songId_String}"
    )
    @ResponseBody
    public ResponseEntity<String> getSongById(
            @PathVariable String songId_String
            ,  @RequestHeader(HttpHeaders.ACCEPT) String accept
    ){
        log.info("getSongById() wird ausgeführt.");
        System.out.println("getSongById() wird ausgeführt.");
        MediaType targetMediaType;
        try {
            targetMediaType = checkMediatype(accept);
        }
        catch (InvalidMediaTypeException ex){
            log.info("Accept-Mediatype(" + accept + ") des Headers ist nicht erlaubt.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!this.checkSongId(songId_String)){
            log.info("Id wird nicht unterstützt.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int id;
            id = Integer.parseInt(songId_String);
            Song song;
            try {
                song = songRepository.getById(id);
            }
            catch (javax.persistence.EntityNotFoundException ex){
                log.info("SongId(" + id + ") wurde nicht in der Datenbank gefunden.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String ausgabe;
            switch (targetMediaType.toString()) {
                case MediaType.ALL_VALUE :
                    ausgabe = song.toJSON();
                    targetMediaType = MediaType.APPLICATION_JSON;
                    break;
                case MediaType.APPLICATION_JSON_VALUE:
                    ausgabe = song.toJSON();
                    break;
                case MediaType.APPLICATION_XML_VALUE:
                    ausgabe = song.toXML();
                    break;
                default:
                    ausgabe = null;
                    break;
            }
            if(ausgabe == null){
                log.info("Song konnte nicht ins Zielformat(" + accept + ") umgewandelt werden.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("Song konnte in das Zielformat(" + targetMediaType + ") erfolgreich umgewandelt werden. Hier die Ausgabe: " + System.getProperty("line.separator") + ausgabe);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(targetMediaType);
            return new ResponseEntity<>(ausgabe, header, HttpStatus.OK);
    }

    /*
     * Funktion gibt sämtliche Songs zurueck
     */
    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> getAllSongs(@RequestHeader("Accept") String accept) {
        System.out.println("getAllSongs() wird ausgefuehrt.");
        //TODO: Response mit XML-Liste
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

    /*
     * response zur aufgabe 6
     */
    @PostMapping(
            consumes = "application/json")
    @ResponseBody
    public void postSong(@RequestBody Song newSong, HttpServletResponse response){
        int newSongId = 1;
        while (songRepository.existsById(newSongId)){
            newSongId++;
        }
        newSong.setId(newSongId);
        log.info("Song wird mit der Id: "+ newSong.getId() +" eingefuegt");
        songRepository.save(newSong);
        response.setStatus(201);
        response.setHeader("Location", ServletUriComponentsBuilder.fromCurrentContextPath().path("/evilcorp42/songs/"+newSong.getId()).toUriString()); //URI des neuen Songs zurueckgeben/als location angeben
    }

    /*
     * response zur aufgabe 7
     */
    @DeleteMapping(
            value="/{songId}",
            headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> deleteSong(@PathVariable int songId){
        songRepository.deleteById(songId);
        log.info("Song mit der Id: "+ songId +" wurde geloescht");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Funktion ermittelt ob die ID eine erlaubte Id ist
     * @param id_String in Stringformat
     * @return true, wenn der Wert erlaubt ist, false wenn der Wert fehlerhaft oder falsch ist
     */
    private boolean checkSongId(String id_String){
        try{
            int idNumber = Integer.parseInt(id_String);
            if(idNumber > 0){
                return true;
            }
        }
        catch (NumberFormatException ex){
            return false;
        }
        return false;
    }


    /**
     * Funktion versucht aus einem String von einen HTTP-Accept-Header-Attribut einen erlaubten Mediatype zu ermitteln
     * @param mt String mit eventuell mehreren Mediatypen die angefragt werden
     * @return den vorrangig verwendeten MediaTyp oder ein NULL wenn kein Mediatype ermitteln werden konnte
     */
    private MediaType checkMediatype(String mt){
        if(mt.contains(MediaType.ALL_VALUE)){
            return MediaType.APPLICATION_JSON;
        }
        else if (mt.contains(MediaType.APPLICATION_JSON_VALUE)){
            return MediaType.APPLICATION_JSON;
        }
        else if (mt.contains(MediaType.APPLICATION_XML_VALUE)){
            return MediaType.APPLICATION_XML;
        }
        else {
            throw new InvalidMediaTypeException("mt","Fehlerhaftes MediaType ermittelt.");
        }
    }
}
