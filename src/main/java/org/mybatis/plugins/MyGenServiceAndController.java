package org.mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGenServiceAndController extends MyMapperPlugin {

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


}
