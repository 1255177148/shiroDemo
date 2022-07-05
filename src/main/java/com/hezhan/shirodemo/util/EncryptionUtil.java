package com.hezhan.shirodemo.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class EncryptionUtil {

    /**
     * 对用户输入的密码进行加密，并返回16进制的字符串
     * @param password 输入的密码
     * @param salt 加密盐
     * @return
     */
    public static String encryption(String password, String salt){
        // 加盐加密，目的是为了让相同的密码通过不同的盐hash散列后的值不同
        ByteSource byteSource = ByteSource.Util.bytes(salt);
        SimpleHash result = new SimpleHash("SHA-1", password, byteSource, 2);
        return result.toHex();
    }
}
