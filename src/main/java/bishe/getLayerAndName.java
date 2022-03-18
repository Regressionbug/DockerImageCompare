package bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.Lists;

import javax.swing.tree.TreeNode;

/**
 * @author Lixuhang
 * @date 2021/12/6
 * @whatItFor
 */
public class getLayerAndName {
    public static void main(String[] args) throws IOException {
//        File file = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\i1c.zip");
        InputStream in = new BufferedInputStream(new FileInputStream("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\p8python.tar"));
        TarArchiveInputStream tarIn = new TarArchiveInputStream(in);
        ArchiveEntry tea = null;
        ArrayList<FileTreeNode> layerTrees = new ArrayList<>();
        while ((tea = tarIn.getNextEntry())!=null){
            if(tea.isDirectory()){
//                System.out.println(ze.getName()+" directory");
//                zin.skip(ze.getSize());
            }
            else {
//                if(ze.getName().equals("manifest.json")) {
//                    findManifestResult(zin);
//                }
                if(tea.getName().contains("layer.tar")){
                    layerTrees.add(analyseTarAndBuildTree(tarIn));
                }
            }
        }

        InputStream in2 = new BufferedInputStream(new FileInputStream("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\p10python.tar"));
        TarArchiveInputStream tarIn2 = new TarArchiveInputStream(in2);
        ArchiveEntry tea2 = null;
        ArrayList<FileTreeNode> layerTrees2 = new ArrayList<>();
        while ((tea2 = tarIn2.getNextEntry())!=null){
            if(tea2.isDirectory()){
//                System.out.println(ze.getName()+" directory");
//                zin.skip(ze.getSize());
            }
            else {
//                if(ze.getName().equals("manifest.json")) {
//                    findManifestResult(zin);
//                }
                if(tea2.getName().contains("layer.tar")){
                    layerTrees2.add(analyseTarAndBuildTree(tarIn2));
                }
            }
        }

        ArrayList<Map> allResult = new ArrayList<>();
        Map<FileTreeNode,FileTreeNode> result = new HashMap<>();
        for(FileTreeNode node1 : layerTrees){
            for(FileTreeNode node2 : layerTrees2){
                result = BuildTree.findAllSameTree(node1,node2);
                if(result.size() > 0){
                    allResult.add(result);
                }
            }
        }
        System.out.println("ss");
    }

    public static ArrayList<String> findManifestResult(ZipInputStream zipInputStream){
        ArrayList<String> layers = new ArrayList<>();
        String jsonStr = readJsonFile(zipInputStream);
        if(jsonStr == null){
            return layers;
        }
        JSONArray array = JSONArray.parseArray(jsonStr);
        JSONObject object = array.getJSONObject(0);
        JSONArray layersJSON = (JSONArray)object.get("Layers");
        for(Object str : layersJSON){
            String trans = (String)str;
            trans = trans.replaceAll("/layer.tar","");
            layers.add(trans);
        }
        return layers;
    }

    public static String readJsonFile(ZipInputStream zipInputStream) {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(zipInputStream);
        while(scanner.hasNextLine()){
            sb.append(scanner.nextLine());
        }
        return sb.toString();
    }

    public static FileTreeNode analyseTarAndBuildTree(TarArchiveInputStream fileInputStream) throws IOException {
        TarArchiveInputStream inputStream = new TarArchiveInputStream(fileInputStream);
        ArchiveEntry tae = null;
        int tempLevel = 0;
        //由于是深度优先，所以这个treeStruct相当于从根节点到目前访问点的通路记录，对于整体的
        LinkedList<FileTreeNode> treeStruct = new LinkedList<>();
        FileTreeNode.clearTreeId();
        FileTreeNode node = new FileTreeNode(FileTreeNode.getTreeId(),"layer1",0);
        treeStruct.add(node);
        while ((tae = inputStream.getNextEntry())!= null) {
            if(tae.isDirectory()){
                String dirStr = tae.getName();
                System.out.println(dirStr);
                FileTreeNode tempNode = new FileTreeNode(FileTreeNode.getTreeId());
                //通过除号的数量来标识级别
                getDirLevelAndName(tae.getName(),tempNode);
                tempLevel = tempNode.getLevel();
                //但是新的级别出现（就是大于当前treeStruct的数量）
                if(tempLevel > treeStruct.size()-1){
                    incTreeStructNum(treeStruct);
                }
                //一旦没有新的级别出现，就说明treeStruct有节点要出去了
                else {
                    int removeSize = treeStruct.size() - tempLevel;
                    for(int i = 0;i < removeSize;i++){
                        FileTreeNode removeNode = treeStruct.removeLast();
                        //调整每个节点的子节点的按照排序（之所以需要排序，是因为保证两个镜像是一致的）
                        adjustFileNode(removeNode);
                    }
                }
                //treeStruct暂时确定之后，就可以增加其中所有节点的子节点数量
               incTreeStructSubNodeNum(treeStruct);
                //调整完之后，将最新节点加入treeStruct
                treeStruct.add(tempNode);
                //设置父子节点的关系
                FileTreeNode fatherNode = treeStruct.get(tempLevel-1);
                tempNode.setFatherNode(fatherNode);
                ArrayList<FileTreeNode> subNode = fatherNode.getSubNode();
                subNode.add(tempNode);
            }
        }
        //对于剩下的文件夹进行调整
        if(!treeStruct.isEmpty()){
            for(FileTreeNode getNode : treeStruct){
                adjustFileNode(getNode);
            }
        }
        return node;
    }

    public static void getDirLevelAndName(String str,FileTreeNode node){
        int level = 0;
        String[] strLevels = str.split("/");
        if(strLevels.length == 0){
            return;
        }
        node.setLevel(strLevels.length);
        node.setNodeName(strLevels[strLevels.length-1]);
    }

    public static void adjustFileNode(FileTreeNode node){
        ArrayList<FileTreeNode> subNode = node.getSubNode();

        Collections.sort(subNode, new Comparator<FileTreeNode>() {
            @Override
            public int compare(FileTreeNode o1, FileTreeNode o2) {
                //数量相对于深度更加有辨识度，所以作为判断的第一标准
                int subNum1 = o1.getSubNodeNum();
                int subNum2 = o2.getSubNodeNum();
                if(subNum1 != subNum2){
                    return subNum1-subNum2;
                }
                int subHigh1 = o1.getSubTreeHigh();
                int subHigt2 = o2.getSubTreeHigh();
                if(subHigh1 != subHigt2){
                    return subHigh1-subHigt2;
                }
                String str1 = o1.getNodeName();
                String str2 = o2.getNodeName();
                if(str1.length()!=str2.length()){
                    return str1.length()-str2.length();
                }
                for(int i = 0 ; i < str1.length() ; i++){
                    char char1 = str1.charAt(i);
                    char char2 = str2.charAt(i);
                    if(char1 != char2){
                        return char1-char2;
                    }
                }
                return 0;
            }
        });
    }

    //增加treeStruct中所有节点树高
    public static void incTreeStructNum(LinkedList<FileTreeNode> treeStruct){
        for(FileTreeNode node : treeStruct){
            node.incSubTreeHigh();
        }
    }
    //增加treeStruct中所有节点子节点数量
    public static void incTreeStructSubNodeNum(LinkedList<FileTreeNode> treeStruct){
        for(FileTreeNode node : treeStruct){
            node.incSubTreeNum();
        }
    }


    public static void incTreeStructLevel(LinkedList<FileTreeNode> treeStruct){
        int level = treeStruct.size();
        int tempIndex = 0;
        for(FileTreeNode node : treeStruct){
            int tempMaxSubHigh = level-tempIndex;
            if(tempMaxSubHigh > node.getSubTreeHigh()){
                node.setSubTreeHigh(tempMaxSubHigh);
            }
        }
    }



}
