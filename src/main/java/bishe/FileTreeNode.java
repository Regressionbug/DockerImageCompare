package bishe;

import java.util.ArrayList;

/**
 * @author Lixuhang
 * @date 2021/12/18
 * @whatItFor
 */
public class
FileTreeNode {
    int nodeId;
    ArrayList<FileTreeNode> subNode = new ArrayList<>();
    String nodeName;
    int level;
    int subNodeNum = 0;

    public FileTreeNode getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(FileTreeNode fatherNode) {
        this.fatherNode = fatherNode;
    }

    int subTreeHigh = 1;
    FileTreeNode fatherNode;
    private static int treeId = 0;

    public FileTreeNode() {}

    public FileTreeNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public FileTreeNode(int nodeId, String nodeName, int level) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.level = level;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public ArrayList<FileTreeNode> getSubNode() {
        return subNode;
    }

    public void setSubNode(ArrayList<FileTreeNode> subNode) {
        this.subNode = subNode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public static int getTreeId(){
        int returnNum = treeId;
        treeId = treeId + 1;
        return treeId;
    }

    public static void clearTreeId(){
        treeId = 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSubNodeNum() {
        return subNodeNum;
    }

    public void setSubNodeNum(int subNodeNum) {
        this.subNodeNum = subNodeNum;
    }

    public int getSubTreeHigh() {
        return subTreeHigh;
    }

    public void setSubTreeHigh(int subTreeHigh) {
        this.subTreeHigh = subTreeHigh;
    }

    public void incSubTreeNum(){
        subNodeNum++;
    }

    public void incSubTreeHigh(){ subTreeHigh++;}
}
