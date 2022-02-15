package bishe;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Lixuhang
 * @date 2021/12/19
 * @ whatItFor
 */
public class BuildTree {
    public static void main(String[] args) throws IOException {
        File file1 = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\noEntry1.tar");
        FileInputStream inputStream = new FileInputStream(file1);
        FileTreeNode node1 = getLayerAndName.analyseTarAndBuildTree(inputStream);
        File file2 = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\noEntry2.tar");
        FileInputStream inputStream1 = new FileInputStream(file2);
        FileTreeNode node2 = getLayerAndName.analyseTarAndBuildTree(inputStream1);
        Map<FileTreeNode,FileTreeNode> result = findAllSameTree(node1,node2);
        System.out.println(result);

    }

    //获取两个树节点的各自的非叶子节点
    public static Map<FileTreeNode,FileTreeNode> findAllSameTree(FileTreeNode node1,FileTreeNode node2){
        ArrayList<FileTreeNode> compareFileNodes = getNonleafTreeNodeBFS(node2);
        //采用深度优先遍历进行比较
        Map<FileTreeNode,FileTreeNode> result = new HashMap<>();
        for(FileTreeNode compareFileNode : compareFileNodes){
            if(compareFileNode.getNodeName().equals("file1")){
                System.out.println("a");
            }
            SameNodeResult tryResult = new SameNodeResult();
            findSameNode(node1,compareFileNode,tryResult);
            if(tryResult.isFind()){
                result.put(compareFileNode,tryResult.getTargetNode());
            }
        }
        return result;
//        ArrayList<FileTreeNode> tryFileNodes = getNonleafTreeNodeBFS(node1);
//
//        for(FileTreeNode tryFileNode : tryFileNodes){
//            for(FileTreeNode compareFileNode : compareFileNodes){
//                //基于尝试比较和
//                ArrayList<FileTreeNode> similarNodes = findSimilarNode(tryFileNode,compareFileNode);
//                for(FileTreeNode similarNode : similarNodes){
//                    if(compareTree(similarNode,tryFileNode)){
//
//                    }
//                }
//            }
//        }
//        for(FileTreeNode node : record){
//            ArrayList<FileTreeNode> similarNodes = findSimilarNode(node1,node);
//            for(FileTreeNode similarNode : similarNodes){
//                if(compareTree(similarNode,node1)){
//
//                }
//            }
//        }

    }
    //在targetNode为根节点树中的非叶子节点中，寻找findNode的相似节点(接下来进行多叉树的深度优先先序遍历
    public static boolean findSameNode(FileTreeNode findNode,FileTreeNode targetNode,SameNodeResult result){
        //true和false用来判断是否继续，用result是否为null来进行

        //判断非叶子节点
        if(findNode.getSubNodeNum() == 0){
            //我的子节点数为0了，后面的其他同级节点肯定也等于0
            return false;
        }
        //由于是先序遍历，所以先进行判断
        NodeCompareResult tempResult = compareNode(findNode,targetNode);
        if(tempResult.isTarget()){
           boolean isResult = compareTree(findNode,targetNode);
           if(isResult){
               result.setFind(true);
               result.setTargetNode(findNode);
               return false;
           }
        }
        if(!tempResult.isContinue()){
            //该节点不行，则同级剩下的也不行，就不继续了
            return false;
        }
        //多叉树深度优先遍历
        ArrayList<FileTreeNode> subNodes = findNode.getSubNode();
        for(FileTreeNode subNode : subNodes){
            boolean isContinue = findSameNode(subNode,targetNode,result);
            if(!isContinue){
                if(result.isFind()){
                    return false;
                }
                break;
            }
        }
        return true;
    }

    public static NodeCompareResult compareNode(FileTreeNode findNode, FileTreeNode targetNode){
        NodeCompareResult result = new NodeCompareResult();
        //如果名字、子节点数量和层数都相等，则认为其是可以用来比较的完全相似点
        if(findNode.getNodeName().equals(targetNode.getNodeName())){
            if(findNode.getSubNodeNum() == targetNode.getSubNodeNum()){
                if(findNode.getSubTreeHigh() == targetNode.getSubTreeHigh()){
                    result.setTarget(true);
                }
            }
        }
        else{
            result.setTarget(false);
        }
        //名字不同，则不可能属于完全相似点，则需要进行是否可以继续判断子节点是否
        if(findNode.getSubNodeNum() < targetNode.getSubNodeNum()){
            result.setContinue(false);
            return result;
        }
        if(findNode.getSubNodeNum() == targetNode.getSubNodeNum()){
            if(findNode.getSubTreeHigh() < targetNode.getSubTreeHigh()){
                result.setContinue(false);
                return result;
            }
        }
        result.setContinue(true);
        return result;
    }
    //比较两个树节点是否完全相同，这里使用了深度优先遍历
    public static boolean compareTree(FileTreeNode node1,FileTreeNode node2){
        if((!node1.getNodeName().equals(node2.getNodeName())) ||
                (node1.getSubNodeNum()!=node2.getSubNodeNum()) ||
                    (node1.getSubTreeHigh() != node2.getSubTreeHigh())){
            return false;
        }
        ArrayList<FileTreeNode> subNodes1 = node1.getSubNode();
        ArrayList<FileTreeNode> subNodes2 = node2.getSubNode();
        for(int i = 0;i < subNodes1.size(); i++){
            FileTreeNode getNode1 = subNodes1.get(i);
            FileTreeNode getNode2 = subNodes2.get(i);
            if(!compareTree(getNode1,getNode2)){
                return false;
            }
        }
        return true;
    }

    //用于广度优先获取一个树的所有非叶子节点
    public static ArrayList<FileTreeNode> getNonleafTreeNodeBFS(FileTreeNode root) {
        ArrayList<FileTreeNode> result = new ArrayList<>();
        LinkedList<FileTreeNode> record = new LinkedList<>();
        record.add(root);
        while(!record.isEmpty()){
            FileTreeNode tempNode = record.remove();
            //如果该节点是存在子节点的，则加入结果中，并把其子节点中加入下一次遍历中
            if(tempNode.getSubNodeNum() != 0){
                result.add(tempNode);
                record.addAll(tempNode.getSubNode());
            }
        }
        return result;
    }

}
