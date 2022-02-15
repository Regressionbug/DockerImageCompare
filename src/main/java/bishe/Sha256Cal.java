package bishe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Lixuhang
 * @date 2021/12/7
 * @whatItFor
 */
public class Sha256Cal {
    public static void main(String[] args)  {
        String str1 = "D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\i1lay1.tar";
        String str2 = "D:\\Learn-Thing\\编程语言\\java\\项目\\测试\\src\\main\\resources\\i2lay.tar";
        byte[] bytes1 = solve(str1);
        byte[] bytes2 = solve(str2);
        System.out.println(Arrays.toString(bytes1));
        System.out.println(Arrays.toString(bytes2));
        System.out.println(bytes1.toString());
        System.out.println(bytes2.toString());
        System.out.println(Arrays.compare(bytes1, bytes2));
    }

    public static byte[] solve(String str)  {
        int buff = 16384;
        byte[] partialHash = null;
        try {
            RandomAccessFile file = new RandomAccessFile(str,"r");
            long startTime = System.currentTimeMillis();
            MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[buff];

            long read = 0;
            long offset = file.length();
            int unitsize;
            while(read < offset) {
                unitsize = (int)(((offset-read)>=buff)? buff : (offset - read));
                file.read(buffer,0,unitsize);
                hashSum.update(buffer,0,unitsize);
                read+=unitsize;
            }
            file.close();
            partialHash = new byte[hashSum.getDigestLength()];
            partialHash = hashSum.digest();
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return  partialHash;
    }
}
