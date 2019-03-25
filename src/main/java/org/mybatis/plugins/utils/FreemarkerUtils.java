package org.mybatis.plugins.utils;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FreemarkerUtils {
    protected static final Logger logger = LoggerFactory.getLogger(FreemarkerUtils.class);
    // 模板存放位置
    protected static String TEMPLATE_FILE_PATH;

    private static Configuration configuration = null;

    static {
        init();
        initFreemarkerConfiguration();
    }

    /**
     * Freemarker 模板环境配置
     * @return
     * @throws IOException
     */
    private static Configuration initFreemarkerConfiguration() {
        Configuration cfg = null;
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        } catch (IOException e) {
            throw new RuntimeException("Freemarker 模板环境初始化异常!", e);
        }
        return cfg;
    }


    /**
     * 初始化配置信息
     */
    private static void init() {
        File file = new File("src/main/resources/template/");
        TEMPLATE_FILE_PATH = file.getPath();
    }








}
