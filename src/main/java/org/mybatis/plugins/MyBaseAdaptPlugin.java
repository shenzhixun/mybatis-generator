package org.mybatis.plugins;

import org.mybatis.generator.api.PluginAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyBaseAdaptPlugin extends PluginAdapter {


    public static CodeGeneratorConfig config = new CodeGeneratorConfig();

    @Override
    public boolean validate(List<String> list) {
        return true;
    }



    protected boolean checkAndCreateFile(String filePath) {
        // 查看文件是否存在, 不存在则创建
        if(filePath==null || filePath.trim()=="") {
            return false;
        }
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                if(config.FILE_OVER_WRITE) {
                    file.delete();
                    file.createNewFile();
                }
                System.err.println(file + " already exists, it was override:" + config.FILE_OVER_WRITE);
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return file.exists();
    }

    protected boolean checkAndMakeDir(String fileDir) {
        // 查看目录是否存在, 不存在则创建
        if(fileDir==null || fileDir.trim()=="") {
            return false;
        }
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists();
    }


    protected String date2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }








}
