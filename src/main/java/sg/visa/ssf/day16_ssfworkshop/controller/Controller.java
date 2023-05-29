package sg.visa.ssf.day16_ssfworkshop.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.visa.ssf.day16_ssfworkshop.model.Boardgame;
import sg.visa.ssf.day16_ssfworkshop.repository.BoardgameRedis;

@RestController
@RequestMapping(path = "api", produces = "application/json")
public class Controller {

    private Integer updateCount = 0;

    @Autowired
    BoardgameRedis repository;
    
    @PostMapping(path = "/boardgame")
    public ResponseEntity<String> createBoardgameEntity(@RequestBody String payload)  {
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader jr = Json.createReader(is);
            body = jr.readObject();
        }   catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }

        Boardgame bg = new Boardgame();
        bg.setJsonObject(body.toString());

        repository.createBoardgameEntity(bg);

        final JsonObject resp = Json.createObjectBuilder()
                .add("insert_count", 1)
                .add("id", bg.getId())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp.toString());
    }

    @GetMapping(path= "/boardgame/{bgId}")
    public ResponseEntity<String> getBoardGameEntity(@PathVariable String bgId) {
        Optional<Object> boardGameOptional = repository.getBoardGameEntity(bgId);
        JsonObject resp;
        if(boardGameOptional.isPresent()) {
            Boardgame bg = (Boardgame) boardGameOptional.get();
            String bgbody = bg.getJsonObject();
            JsonReader jr = Json.createReader(new StringReader(bgbody));
            resp = jr.readObject();
        } else {
            resp = Json.createObjectBuilder()
                .add("error", "Boardgame not found")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.toString());
        }

        return ResponseEntity.ok().body(resp.toString());
    }

    @PutMapping(path = "/boardgame/{bgId}")
    public ResponseEntity<String> updateBoardGameEntity(@RequestBody String payload, @PathVariable String bgId) {

        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader jr = Json.createReader(is);
            body = jr.readObject();
        }   catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }

        Boardgame bg = new Boardgame();
        bg.setId(bgId);
        bg.setJsonObject(body.toString());
        Integer updateStatus = repository.updateBoardGameEntity(bg, bgId);
        JsonObject resp;
        if(updateStatus == 1) {
            updateCount++;
            resp = Json.createObjectBuilder()
                .add("update_count", updateCount)
                .add("id", bgId)
                .build();
        } else {
            resp = Json.createObjectBuilder()
                .add("error", "Boardgame not found")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp.toString());
        }

        return ResponseEntity.ok().body(resp.toString());
    }
}
