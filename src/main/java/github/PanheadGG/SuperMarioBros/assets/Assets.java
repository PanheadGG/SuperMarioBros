package github.PanheadGG.SuperMarioBros.assets;

import com.alibaba.fastjson2.JSONObject;
import github.PanheadGG.SuperMarioBros.utils.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class Assets {
    static private HashMap<String, URL> urlMap = new HashMap<>();
    static private HashMap<String, Sprite> spriteMap = new HashMap<>();
    static public Font FONT;

    public static final BufferedImage UNKNOWN_TEXTURE = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);


    //    static Map<String,URL> sounds = new HashMap<>();
    static {

        int magenta = new Color(0xF800F8).getRGB();
        int black = Color.BLACK.getRGB();

        UNKNOWN_TEXTURE.setRGB(0, 0, magenta); // 左上
        UNKNOWN_TEXTURE.setRGB(1, 0, black);   // 右上
        UNKNOWN_TEXTURE.setRGB(0, 1, black);   // 左下
        UNKNOWN_TEXTURE.setRGB(1, 1, magenta); // 右下

        FONT = new Font("Console", Font.BOLD, 16);
        try (InputStream is = Assets.class.getResourceAsStream("/assets/resources/super_mario_bros/fonts/mario-font.ttf")) {
            if (is != null) {
                FONT = Font.createFont(Font.TRUETYPE_FONT, is);
                FONT = FONT.deriveFont(Font.BOLD, 25f);

            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        JSONObject texturesJson = JSONObject.parseObject(FileUtil.readFile(getAssetURL("textures/textures.json")));
        JSONObject soundsJson = JSONObject.parseObject(FileUtil.readFile(getAssetURL("sounds/sounds.json")));
        for (String key : texturesJson.keySet()) {
            if (texturesJson.getJSONObject(key).getString("type").equals("sprite")) {
                String spriteFilePath = texturesJson.getJSONObject(key).getString("src");
                JSONObject spriteJson = JSONObject.parseObject(FileUtil.readFile(getAssetURL(spriteFilePath)));
//                System.out.println(spriteJson.getString("src"));
//                System.out.println(getAssetURL(spriteJson.getString("src").trim()));
                Sprite sprite = new Sprite(
                        spriteJson.getIntValue("columns"),
                        getAssetURL(spriteJson.getString("src")),
                        spriteJson.getIntValue("fps")
                );
                spriteMap.put(key, sprite);
                continue;
            }
            urlMap.put(key, getAssetURL(texturesJson.getJSONObject(key).getString("src").trim()));
        }
        for (String key : soundsJson.keySet()) {
            urlMap.put(key, getAssetURL(soundsJson.getJSONObject(key).getString("src").trim()));
        }
    }

    private static URL getAssetURL(String path) {
        if (path == null || path.isEmpty()) return null;
        if (path.startsWith("/") || path.startsWith("\\")) path = path.substring(1);
//        System.out.println(path);
        return Assets.class.getResource("/assets/resources/super_mario_bros/" + path);
    }

    public static URL getURLByKey(String key) {
        return urlMap.get(key);
    }

    public static BufferedImage getImage(URL url) {
        if (url == null) {
            return Assets.UNKNOWN_TEXTURE;
        }
        try {
            BufferedImage image = ImageIO.read(url);
            if (image != null) return image;
        } catch (NullPointerException | IOException e) {
            return Assets.UNKNOWN_TEXTURE;
        }
        return Assets.UNKNOWN_TEXTURE;
    }

    public static BufferedImage getImageByKey(String key){
        return getImage(getURLByKey(key));
    }

    public static DynamicImage getDynamicImageByKey(String key) {
        Sprite sprite = spriteMap.get(key);
        if (sprite != null) {
            return sprite.getDynamicImage();
        }
        return new DynamicImage(Assets.UNKNOWN_TEXTURE);
    }
    public static Font getFont(float size){
        return FONT.deriveFont(Font.BOLD, size);
    }
}
