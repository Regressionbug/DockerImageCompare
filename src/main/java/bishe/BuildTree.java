package bishe;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @author Lixuhang
 * @date 2021/12/19
 * @ whatItFor
 */
public class BuildTree {
    public static void main(String[] args) throws IOException {
        File file1 = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\layer8.7.tar");
        FileInputStream inputStream = new FileInputStream(file1);
//        FileTreeNode node1 = getLayerAndName.analyseTarAndBuildTree((ZipInputStream) inputStream);
        File file2 = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\layer10.7.1.tar");
        FileInputStream inputStream1 = new FileInputStream(file2);
//        FileTreeNode node2 = getLayerAndName.analyseTarAndBuildTree(inputStream1);
//        Map<FileTreeNode,FileTreeNode> result = findAllSameTree(node1,node2);
//        System.out.println(result);

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
    /**
     *
     * @param findNode 在其中寻找的树
     * @param targetNode 需要找到的相似单节点
     * @param result 结果
     * @return 用于表示是否继续，我们认为在一个树中只有一个相似的单节点
     */
    public static boolean findSameNode(FileTreeNode findNode,FileTreeNode targetNode,SameNodeResult result){
        //判断非叶子节点
        if(findNode.getSubNodeNum() == 0){
            //我的子节点数为0了，后面的其他同级节点肯定也等于0
            return false;
        }
        //由于是先序遍历，所以先进行判断
        NodeCompareResult tempResult = compareNode(findNode,targetNode);
        if(tempResult.isTarget()){
           boolean isResult = compareTree(findNode,targetNode,true);
           if(isResult){
               result.setFind(true);
               result.setTargetNode(findNode);
               //找到目标的节点
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
        //子节点数量和层数都相等，则认为其是可以用来比较的完全相似点(名字不同，当其子节点都相同，也有可能属于相似的节点)
            if(findNode.getSubNodeNum() == targetNode.getSubNodeNum()){
                if(findNode.getSubTreeHigh() == targetNode.getSubTreeHigh()){
                    result.setTarget(true);
                }
            }
        else{
            result.setTarget(false);
        }

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

    /**
     * 比较两个树节点是否完全相同或者其子节点是否完全相同，这里使用了深度优先遍历
     * 这个函数需要第三个参数，用于识别第一次迭代（第一次迭代时，我们不需要进行名字相同）
     * 这个方法是，从外部调用，肯定是第一次
     */

    public static boolean compareTree(FileTreeNode node1,FileTreeNode node2,boolean isFirst){
        //第一次判断相同时，则不需要名字
        if(isFirst){
            if ((node1.getSubNodeNum()!=node2.getSubNodeNum()) ||
                    (node1.getSubTreeHigh() != node2.getSubTreeHigh())){
                return false;
            }
        }
        //之后的判断相同，需要判断名字
        else {
            if((!node1.getNodeName().equals(node2.getNodeName())) ||
                    (node1.getSubNodeNum()!=node2.getSubNodeNum()) ||
                    (node1.getSubTreeHigh() != node2.getSubTreeHigh())){
                return false;
            }
        }

        ArrayList<FileTreeNode> subNodes1 = node1.getSubNode();
        ArrayList<FileTreeNode> subNodes2 = node2.getSubNode();
        for(int i = 0;i < subNodes1.size(); i++){
            FileTreeNode getNode1 = subNodes1.get(i);
            FileTreeNode getNode2 = subNodes2.get(i);
            if(!compareTree(getNode1,getNode2,false)){
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
