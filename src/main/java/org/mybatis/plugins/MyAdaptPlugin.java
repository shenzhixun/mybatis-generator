package org.mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdaptPlugin extends PluginAdapter {

    /** 文件覆盖 */
    public static final boolean FILE_OVER_WRITE = true;

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.Builder");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");

        //添加domain的注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine("* Created by Mybatis Generator on " + date2Str(new Date()));
        topLevelClass.addJavaDocLine("*/");

        return true;
    }


    /**
     * 额外再生成 service 和 controller
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String javaRepositoryPackage = this.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
        String javaMapperType = introspectedTable.getMyBatis3JavaMapperType();
        String topPackage = javaRepositoryPackage.substring(0, javaRepositoryPackage.lastIndexOf('.'));
        String javaClassName = javaMapperType.substring(javaMapperType.lastIndexOf('.') + 1, javaMapperType.length()).replace("Mapper", "");
        String targetProject = this.getContext().getJavaClientGeneratorConfiguration().getTargetProject();

        Map<String, String> root = new HashMap<String, String>();
        root.put("topPackage", topPackage);
        root.put("EntityName", javaClassName);
        root.put("entityName", new StringBuilder().append(Character.toLowerCase(javaClassName.charAt(0)))
                .append(javaClassName.substring(1)).toString());


        genService(targetProject, topPackage, javaClassName, root, introspectedTable);
        genServiceImpl(targetProject, topPackage, javaClassName, root);

        genController(targetProject, topPackage, javaClassName, root);

        return null;

    }

    @SuppressWarnings("deprecation")
    private void genService(String targetProject, String topPackage, String javaClassName, Map<String, String> root, IntrospectedTable introspectedTable) {
        try {
            String dirPath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/service";
            String serviceFile = dirPath + "/" + javaClassName + "Service.java";

            checkAndMakeDir(dirPath);
            checkAndCreateFile(serviceFile);


            






        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genServiceImpl(String targetProject, String topPackage, String javaClassName, Map<String, String> root) {
        try {
            String dirPath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/service";
            String filePath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/service/" + javaClassName
                    + "ServiceImpl.java";
            File dir = new File(dirPath);
            File file = new File(filePath);
            if (file.exists()) {
                System.err.println(file + " already exists, it was skipped.");
                return;
            } else {
                dir.mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @SuppressWarnings("deprecation")
    private void genController(String targetProject, String topPackage, String javaClassName, Map<String, String> root) {
        try {

            String dirPath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/controller";
            String filePath = targetProject + "/" + topPackage.replaceAll("\\.", "/") + "/controller/" + javaClassName
                    + "Controller.java";
            File dir = new File(dirPath);
            File file = new File(filePath);
            if (file.exists()) {
                System.err.println(file + " already exists, it was skipped.");
                return;
            } else {
                try {
                    dir.mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private boolean checkAndCreateFile(String filePath) {
        // 查看文件是否存在, 不存在则创建
        if(filePath==null || filePath.trim()=="") {
            return false;
        }
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                if(FILE_OVER_WRITE) {
                    file.delete();
                    file.createNewFile();
                }
                System.err.println(file + " already exists, it was override:" + FILE_OVER_WRITE);
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return file.exists();
    }

    private boolean checkAndMakeDir(String fileDir) {
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


    private String date2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

}
