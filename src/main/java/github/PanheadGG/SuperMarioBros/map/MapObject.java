package github.PanheadGG.SuperMarioBros.map;

public class MapObject {
    public String model;
    public boolean hasReward;
    public String[] rewards;
    public MapObject(String model){
        this.model = model;
        hasReward = false;
        rewards = new String[0];
    }
    public MapObject(String model,String[] rewards){
        this.model = model;
        this.hasReward = true;
        this.rewards = rewards;
    }
}
