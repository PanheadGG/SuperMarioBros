package github.PanheadGG.SuperMarioBros.map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import github.PanheadGG.SuperMarioBros.core.GameEngine;
import github.PanheadGG.SuperMarioBros.model.block.Brick;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Mushroom;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;
import github.PanheadGG.SuperMarioBros.utils.FileUtil;

import java.net.URL;

public class MapLoader {
    private GroundMap map;
    private GameEngine engine;
    private int gameTickRate;
    private int pixelPerUnit;
    public MapLoader(){
    }
    public void setGroundMap(GameEngine engine){
        map = new GroundMap(engine);
        this.engine = engine;
        pixelPerUnit = engine.getPixelPerUnit();
        gameTickRate = engine.getGameTickRate();
    }
    public void load(URL url){
        if(map==null) return;
        String mapJsonStr = FileUtil.readFile(url);
        JSONObject mapJson = JSONObject.parseObject(mapJsonStr);
        JSONObject background = mapJson.getJSONObject("map").getJSONObject("background");
        JSONArray backgrounds = background.getJSONArray("set");
        int y=0;
        for (Object line : backgrounds) {
            int x=0;
            if(line instanceof String){
                char[] chars = ((String) line).toCharArray();
                for(char c:chars){
                    if(c=='1'){
                        map.add(new Brick(pixelPerUnit, x, y));
                    }
                    x++;
                }
            }
            y++;
        }

        JSONObject entities = mapJson.getJSONObject("map").getJSONObject("entities");
        JSONObject playerObject = entities.getJSONObject("player");
        double playerX = playerObject.getDoubleValue("x");
        double playerY = playerObject.getDoubleValue("y");
        Mario mario = new Mario(gameTickRate, pixelPerUnit);
        mario.setPosition(playerX, playerY);
        map.setPlayer(mario);

        JSONObject enemies = entities.getJSONObject("enemies");
        JSONArray enemiesArray = enemies.getJSONArray("set");
        for (Object enemy : enemiesArray) {
            if(enemy instanceof JSONObject){
                JSONObject enemyObject = (JSONObject) enemy;
                double enemyX = enemyObject.getDoubleValue("x");
                double enemyY = enemyObject.getDoubleValue("y");
                Mushroom mushroom = new Mushroom(gameTickRate, pixelPerUnit);
                mushroom.setPosition(enemyX, enemyY);
                map.add(mushroom);
            }
        }

    }

    public GroundMap getGroundMap(){
        return map;
    }

}
