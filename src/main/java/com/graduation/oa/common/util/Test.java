package com.graduation.oa.common.util;

import com.graduation.oa.common.util.EncryptUtil;

import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String args[]) throws UnsupportedEncodingException {
        String password = "666666";
        System.out.print("aaaaaaaaaaaaaaa"+ EncryptUtil.base64Decode(password));
    }
}
