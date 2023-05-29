package sg.visa.ssf.day16_ssfworkshop.model;

import java.io.Serializable;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("boardgames")
public class Boardgame implements Serializable {

    @Id
    private String boardGameId;
    private String jsonObject;


    public Boardgame() {
        this.boardGameId = generateId();
    }

    public String getId() {
        return boardGameId;
    }
    public void setId(String boardGameId) {
        this.boardGameId = boardGameId;
    }
    public String getJsonObject() {
        return jsonObject;
    }
    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }
    
    public String generateId() {
        Random r = new Random();
        String s = new String();
        int i = 0;
        while(i < 8) {
            s = String.join(s, Integer.toHexString(r.nextInt()));
            i++;
        }

        return s;
    }
}
