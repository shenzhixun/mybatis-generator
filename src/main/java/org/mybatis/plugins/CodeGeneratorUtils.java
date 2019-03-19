package org.mybatis.plugins;

import com.google.common.base.CaseFormat;

public class CodeGeneratorUtils {


    /**
     * 下划线转成驼峰, 首字符为小写
     * eg: gen_test_demo ==> genTestDemo
     * @param tableName 表名, eg: gen_test_demo
     * @return
     */
    public static String nameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    /**
     * 下划线转成驼峰, 首字符为大写
     * eg: gen_test_demo ==> GenTestDemo
     * @param tableName 表名, eg: gen_test_demo
     * @return
     */
    public static String nameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }








}
