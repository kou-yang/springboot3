package com.conggua.common.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author ky
 * @description 编解码工具
 * @date 2024-04-15 16:25
 */
public class EncodeUtils {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    /**
     * BASE64 编码
     *
     * @param text 编码内容
     * @return 编码后的内容
     * @throws UnsupportedEncodingException
     */
    public static String encode(String text) throws UnsupportedEncodingException {
        return BASE64_ENCODER.encodeToString(text.getBytes(DEFAULT_URL_ENCODING));
    }

    /**
     * BASE64 解码
     *
     * @param encodedText 解码内容
     * @return 解码后的内容
     * @throws UnsupportedEncodingException
     */
    public static String decode(String encodedText) throws UnsupportedEncodingException {
        return new String(BASE64_DECODER.decode(encodedText), DEFAULT_URL_ENCODING);
    }

    /**
     * URL 编码
     *
     * @param part 编码内容
     * @return 编码后的内容
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String part) throws UnsupportedEncodingException {
        return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
    }

    /**
     * URL 解码
     *
     * @param part 解码内容
     * @return 解码后的内容
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String part) throws UnsupportedEncodingException {
        return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
    }
}
