package bishe;

import java.util.ArrayList;

/**
 * @author Lixuhang
 * @date 2021/12/6
 * @whatItFor
 */
public class ManifestResult {
    boolean isSame;
    int layerNum;
    ArrayList<String> layers = new ArrayList<>();

    public boolean isSame() {
        return isSame;
    }

    public void setSame(boolean same) {
        isSame = same;
    }

    public int getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(int layerNum) {
        this.layerNum = layerNum;
    }

    public ArrayList<String> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<String> layers) {
        this.layers = layers;
    }
}
