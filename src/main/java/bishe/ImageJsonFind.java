package bishe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Scanner;

/**
 * @author Lixuhang
 * @date 2021/12/8
 * @whatItFor
 */
public class ImageJsonFind {
    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File("D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\image1.json");
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        Scanner sc = new Scanner(inputStreamReader);
        while(sc.hasNextLine()){
            stringBuilder.append(sc.nextLine());
        }
        String str = stringBuilder.toString();
        JSONObject jsonObject = JSON.parseObject(str);
        String config = jsonObject.getString("config");
        JSONObject configObject = JSON.parseObject(config);
        String imageHashStr = configObject.getString("Image");
        String imageHash = imageHashStr.replaceAll("sha256:","");
    }
}
