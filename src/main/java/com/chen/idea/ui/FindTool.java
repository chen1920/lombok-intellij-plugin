package com.chen.idea.ui;

import java.io.File;
import java.util.List;

/**
 * @author chen
 */
public class FindTool {

    private static final String TAG = "FindTool";
    private static String LAYOUT_PATH = "src/main/res/layout";
    private static FindFileTravoler XML_FINDER = new FindFileTravoler(".xml");

    public static String findLayoutXml(String rootDir, String path) {
        try {
            File[] dirs = new File(rootDir).listFiles();
            if (dirs != null) {
                for (File f : dirs) {
                   String ret = processProjectDir(f, path);
                   if(ret!=null){
                       return ret;
                   }
                }
            }
            return "目录为空";
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    /**
     * 处理一个项目目录
     */
    private static String processProjectDir(File projectDir, String path) {
        if (projectDir == null || !projectDir.isDirectory()) {
            return null;
        }
        File layoutDir = new File(projectDir, LAYOUT_PATH);
        if (!layoutDir.isDirectory()) {
            return null;
        }
        List<File> files = XML_FINDER.getFiles();
        files.clear();
        XML_FINDER.travoFile(layoutDir);
        int preLen = layoutDir.getAbsolutePath().length()+1;
        if(!files.isEmpty()){
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                String real = filePath.substring(preLen, filePath.length()-4);
                String uiPath = real.replace('/', '_').replace('\\', '_');
                if(path.equals(uiPath)){
                    return filePath;
                }
            }
        }
        return null;
    }
}
