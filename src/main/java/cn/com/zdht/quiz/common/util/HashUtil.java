package cn.com.zdht.quiz.common.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * @author 袁臻
 * 17-7-27
 */
public class HashUtil {
    /**
     * 获取指定字符串的SHA384哈希值，哈希值字符串长度为96
     *
     * @param source 需要哈希处理的字符串
     * @return 指定字符串的SHA384哈希值
     */
    public static String hashSha384(final String source) {
        return Hashing.sha384()
                .hashString(source, StandardCharsets.UTF_8)
                .toString();
    }
}
