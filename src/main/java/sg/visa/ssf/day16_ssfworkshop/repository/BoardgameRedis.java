package sg.visa.ssf.day16_ssfworkshop.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.visa.ssf.day16_ssfworkshop.model.Boardgame;

@Repository
public class BoardgameRedis {
    @Autowired
    private RedisTemplate<String, Object> template;

    public void createBoardgameEntity(Boardgame bg) {
        template.opsForHash().put("boardGameList", bg.getId(), bg);
    }

    public Optional<Object> getBoardGameEntity(String id) {
        return Optional.ofNullable(template.opsForHash().get("boardGameList", id));
    }

    public Integer updateBoardGameEntity(Boardgame bg, String id) {
        if(template.opsForHash().hasKey("boardGameList", id)){
            template.opsForHash().put("boardGameList", id, bg);
        } else {
            return 0;
        }
        
        return 1;
    }
}
