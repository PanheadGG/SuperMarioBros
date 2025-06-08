package github.PanheadGG.SuperMarioBros.map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.scene.GameEngine;
import github.PanheadGG.SuperMarioBros.model.block.*;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Goomba;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Koopa;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;
import github.PanheadGG.SuperMarioBros.model.item.Castle;
import github.PanheadGG.SuperMarioBros.utils.FileUtil;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;

public class MapLoader {
    private GroundMap map;
    private GameEngine engine;
    private int gameTickRate;
    private int pixelPerUnit;
    private int time = 0;
    private URL backgroundMusicURL;
    private Color backgroundColor = new Color(98, 173, 255);
    private HashMap<Character, MapObject> typeMap = new HashMap<>();

    public MapLoader() {
    }

    public void setGroundMap(GameEngine engine) {
        map = new GroundMap(engine);
        this.engine = engine;
        pixelPerUnit = engine.getPixelPerUnit();
        gameTickRate = engine.getGameTickRate();
    }

    public void load(URL url) {
        if (map == null) return;
        String mapJsonStr = FileUtil.readFile(url);
        JSONObject mapJson = JSONObject.parseObject(mapJsonStr);
        if (mapJson == null || !mapJson.containsKey("map") || !mapJson.getJSONObject("map").containsKey("objects"))
            return;
        if(mapJson.getJSONObject("map").containsKey("name")) map.setMapName(mapJson.getJSONObject("map").getString("name"));
        JSONObject objects = mapJson.getJSONObject("map").getJSONObject("objects");
        for (String key : objects.keySet()) {
            JSONObject object = objects.getJSONObject(key);
            String model = object.getString("model");
            if (object.containsKey("rewards")) {
                int rewardCount = object.getJSONArray("rewards").size();
                String[] rewards = new String[rewardCount];
                for (int i = 0; i < rewardCount; i++) {
//                    System.out.println(object.getJSONArray("rewards").getString(i));
                    rewards[i] = object.getJSONArray("rewards").getString(i);
                }
                typeMap.put(key.charAt(0), new MapObject(model, rewards));
            } else {
                typeMap.put(key.charAt(0), new MapObject(model));
            }
        }
        JSONObject background = mapJson.getJSONObject("map").getJSONObject("background");
        String backgroundColorStr = background.getString("color");
        if (backgroundColorStr != null) {
            backgroundColor = hexToColor(backgroundColorStr);
        }
        String musicKey = mapJson.getJSONObject("map").getString("music").trim();
        time = mapJson.getJSONObject("map").getIntValue("time");
//        SoundManager.playLoop(Assets.getURLByKey(musicKey));
        backgroundMusicURL = Assets.getURLByKey(musicKey);
        JSONArray backgrounds = background.getJSONArray("set");
        int xIndexMax = 0;
        for (int i = 0; i < backgrounds.size(); i++) {
            JSONArray blocks = backgrounds.getJSONArray(i);
            int y = 0;
            int xIndex = xIndexMax;
            for (Object line : blocks) {
                int x = xIndex;
                if (line instanceof String) {
                    char[] chars = ((String) line).toCharArray();
                    for (char c : chars) {
                        if (typeMap.get(c).model.equals("stone")) {
                            map.add(new Stone(pixelPerUnit, x, y));
                        }
                        if (typeMap.get(c).model.equals("brick")) {
                            if (typeMap.get(c).hasReward) {
                                map.add(new Brick(pixelPerUnit,gameTickRate, x, y, typeMap.get(c).rewards));
                            } else {
                                map.add(new Brick(pixelPerUnit,gameTickRate, x, y));
                            }
                        }
                        if (typeMap.get(c).model.equals("tube_upper")) {
                            map.add(new Tube(pixelPerUnit, x, y, Tube.Texture.UPPER));
                        }
                        if (typeMap.get(c).model.equals("tube_lower")) {
                            map.add(new Tube(pixelPerUnit, x, y, Tube.Texture.LOWER));
                        }
                        if (typeMap.get(c).model.equals("lucky_block")) {
                            if (typeMap.get(c).hasReward) {
                                map.add(new LuckyBlock(pixelPerUnit, x, y, gameTickRate, typeMap.get(c).rewards));
                            } else {
                                map.add(new LuckyBlock(pixelPerUnit, x, y, gameTickRate));
                            }
                        }
                        if (typeMap.get(c).model.equals("stage")) {
                            map.add(new StageBlock(pixelPerUnit, x, y));
                        }

                        if (typeMap.get(c).model.equals("goomba")) {
                            Goomba goomba = new Goomba(gameTickRate, pixelPerUnit);
                            goomba.setPosition(x, y);
                            map.add(goomba);
                        }
                        if (typeMap.get(c).model.equals("koopa")) {
                            Koopa koopa = new Koopa(gameTickRate, pixelPerUnit);
                            koopa.setPosition(x, y-0.5);
                            map.add(koopa);
                        }
                        if(typeMap.get(c).model.equals("castle")){
                            map.add(new Castle(gameTickRate,pixelPerUnit, x, y));
                        }
                        x++;
                        xIndexMax = Math.max(xIndexMax, x);
                    }
                }
                y++;
            }
        }

        JSONObject entities = mapJson.getJSONObject("map").getJSONObject("entities");
        JSONObject playerObject = entities.getJSONObject("player");
        double playerX = playerObject.getDoubleValue("x");
        double playerY = playerObject.getDoubleValue("y");
        Player player = new Mario(gameTickRate, pixelPerUnit);
        player.setPosition(playerX, playerY);
        map.setPlayer(player);

        JSONObject enemies = entities.getJSONObject("enemies");
        JSONArray enemiesArray = enemies.getJSONArray("set");
        for (Object enemy : enemiesArray) {
            if (enemy instanceof JSONObject) {
                JSONObject enemyObject = (JSONObject) enemy;
                double enemyX = enemyObject.getDoubleValue("x");
                double enemyY = enemyObject.getDoubleValue("y");
                String model = enemyObject.getString("model");
                if ("goomba".equals(model)) {
                    System.err.println("goomba");
                    Goomba goomba = new Goomba(gameTickRate, pixelPerUnit);
                    goomba.setPosition(enemyX, enemyY);
                    map.add(goomba);
                }
            }
        }

    }

    public GroundMap getGroundMap() {
        return map;
    }

    public URL getBackgroundMusicURL() {
        return backgroundMusicURL;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public int getTime() {
        return time;
    }

    private Color hexToColor(String hex) {
        // 移除井号
        hex = hex.replace("#", "");

        // 解析 RGB 值
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        // 创建 Color 对象
        return new Color(r, g, b);
    }

}
