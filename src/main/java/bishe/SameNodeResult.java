package bishe;

import java.io.File;

/**
 * @author Lixuhang
 * @date 2022/1/7
 * @用于寻找一次遍历中的是否找到
 */

public class SameNodeResult {
    boolean isFind;
    FileTreeNode targetNode;

    public boolean isFind() {
        return isFind;
    }

    public void setFind(boolean find) {
        isFind = find;
    }

    public FileTreeNode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(FileTreeNode targetNode) {
        this.targetNode = targetNode;
    }
}
