package evilcorp42.ex1_songs.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import evilcorp42.ex1_songs.controller.SongController;
import evilcorp42.ex1_songs.entity.Song;
import evilcorp42.ex1_songs.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoadDatabase {

    //Ausgabe in der Konsole
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(SongRepository songRepository) {

        return args -> {
            log.info("====================> Preloading von Datenbank-Content. <====================");

            List<Song> sl = null;
            try{
                Path fileName = Path.of("ex1_songs/src/main/resources/songs.json");
                System.out.println("fileName: " + fileName);
                String songString = Files.readString(fileName);
                ObjectMapper objectMapper = new ObjectMapper();
                sl = objectMapper.readValue(songString, new TypeReference<>() {
                });
                if(sl.size() > 0){
                    System.out.println("Es konnten " + sl.size() + " Songs eingelesen werden.");
                }
                else{
                    System.err.println("Fehler, die Songs konnten nicht eingelesen werden.");
                }
            }
            catch (MalformedURLException ex){
                System.err.println("MalformedURLException wurde in initDatabase() geworfen. Nachricht: " + ex);
            }
            catch (IOException ex){
                System.err.println("IOException wurde in initDatabase() geworfen. Nachricht: " + ex);
            }

            log.info("====================> Song Preloading Start <====================");
            Song s;
            int i = 0;
            if(sl != null) {
                while (i < sl.size()) {
                    s = sl.get(i);
                    log.info("Folgender Song wurde der Datenbank hinzugefügt: " + s.toString());
                    songRepository.save(s);
                    i++;
                }
            }
            log.info("====================> Song Preloading End <====================");


            log.info("====================> Preloading ist abgeschlossen. <====================");
        };
    }
}
