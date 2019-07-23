package com.study.service.impl;

import com.study.service.TechInterface;

/**
 * @author YCKJ1409
 *
 */
public class TechImpl implements TechInterface {

    public String coding(String name) {
        return "程序员："+name+"正在拼命敲代码";
    }
}
