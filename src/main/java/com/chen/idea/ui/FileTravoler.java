package com.chen.idea.ui;


import java.io.File;

/**
 *
 * @author chen
 */
public class FileTravoler {

    private static final String TAG = "FileTravoler";

    /**
     *
     * @param file 文件或目录
     */
    public void travoFile(File file) {
        if (file != null)
        {
            if (file.isDirectory())
            {
                if (processDir(file))
                {
                    try
                    {
                        File[] fs = file.listFiles();
                        if (fs != null)
                        {
                            for (int i = 0; i < fs.length; i++)
                            {
                                if (!fs[i].isDirectory())
                                {
                                    try
                                    {
                                        processFile(fs[i]);
                                    } catch (Throwable e)
                                    {
                                        e.printStackTrace(System.out);
                                    }
                                }
                            }
                            for (int i = 0; i < fs.length; i++)
                            {
                                if (fs[i].isDirectory())
                                {
                                    travoFile(fs[i]);
                                }
                            }
                        }
                    } catch (Throwable e)
                    {
                        e.printStackTrace(System.out);
                    }
                }
            } else if (file.isFile())
            {
                try
                {
                    processFile(file);
                } catch (Throwable e)
                {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    /**
     * 遍历目录
     *
     * @param dir
     * @return
     */
    protected boolean processDir(File dir) {
        return true;
    }

    /**
     * 遍历文件
     *
     * @param file
     * @return
     */
    protected void processFile(File file) {
    }
}
