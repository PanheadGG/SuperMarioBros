package github.PanheadGG.SuperMarioBros.assets;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Asset {
    MARIO_IMAGE("/assets/graphics/mario.png", Type.IMAGE),
    ENEMIES_IMAGE("/assets/graphics/enemies.png", Type.IMAGE),
    TILES_IMAGE("/assets/graphics/tile_set.png", Type.IMAGE),
    ITEMS_IMAGE("/assets/graphics/item_objects.png", Type.IMAGE),
    BACKGROUND_AUDIO("/assets/music/main_theme.ogg",Type.AUDIO),
    UNDER_GROUND_AUDIO("/assets/music/under_ground.mp3",Type.AUDIO),
    JUMP_AUDIO("/assets/sound/small_jump.wav",Type.AUDIO),
    STOMP_AUDIO("/assets/sound/stomp.wav",Type.AUDIO),
    MAIN_SPEED_UP("/assets/music/main_theme_sped_up.ogg",Type.AUDIO),
    MAP_1_BG("/assets/graphics/level_1.png",Type.IMAGE)
    ;

    public static final BufferedImage UNKNOWN_TEXTURE = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);

    static {
        // 定义颜色
        int magenta = new Color(0xF800F8).getRGB();
        int black = Color.BLACK.getRGB();

        // 设置像素
        UNKNOWN_TEXTURE.setRGB(0, 0, magenta); // 左上
        UNKNOWN_TEXTURE.setRGB(1, 0, black);   // 右上
        UNKNOWN_TEXTURE.setRGB(0, 1, black);   // 左下
        UNKNOWN_TEXTURE.setRGB(1, 1, magenta); // 右下

        for(Asset asset:Asset.values()){
            URL url = Asset.class.getResource(asset.path);
            if(url!=null) System.out.printf("Load resource success: %s\n",asset.path);
            else System.err.printf("Load resource failed: %s\n",asset.path);
        }
    }

    private final String path;
    private final Type type;
    Asset(String path, Type type){
        this.path = path;
        this.type = type;
    }

    public URI getURI() {
        try {
            return getClass().getResource(path).toURI();
        } catch (URISyntaxException e) {
            System.err.println(e.getReason());
        }
        return null;
    }

    public URL getURL(){
        return getClass().getResource(path);
    }
    public URL getURL(String path){
        return getClass().getResource(path);
    }


    public String getPath(){
        return path;
    }
    public Type getType(){
        return type;
    }
    public Asset[] getArray(){
        return Asset.values();
    }
    public List<Asset> getList(){
        return new ArrayList<>(Arrays.asList(getArray()));
    }

    public static BufferedImage getImage(Asset asset){
        if(asset.getURL()==null){
            System.err.println("Asset "+asset.getPath()+" not found");
            return Asset.UNKNOWN_TEXTURE;
        }
        if(asset.getType()!=Type.IMAGE){
            System.err.println("Asset "+asset.getPath()+" is not an image");
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(Asset.class.getResource(asset.getPath())));
            if(image!=null) return image;
        } catch (NullPointerException | IOException e) {
            return Asset.UNKNOWN_TEXTURE;
        }
        return Asset.UNKNOWN_TEXTURE;
    }

    public static AudioInputStream getAudioStream(Asset asset) {
        if(asset.getURL()==null){
            System.err.println("Asset "+asset.getPath()+" not found");
            return null;
        }
        if(asset.type!=Asset.Type.AUDIO){
            System.err.println("Asset "+asset.getPath()+" is not audio");
            return null;
        }
        try {
            InputStream audio = Objects.requireNonNull(Asset.class.getResource(asset.getPath())).openStream();
            return AudioSystem.getAudioInputStream(new BufferedInputStream(audio));
        } catch (UnsupportedAudioFileException | IOException e) {
            return null;
        }
    }

    public enum Type {
        IMAGE,AUDIO,FONT
    }

}
