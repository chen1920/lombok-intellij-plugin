package com.chen.idea.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chen
 */
public class FindFileTravoler extends FileTravoler {

    @SuppressWarnings("unused")
    private static final String TAG = "FindFileTravoler";
    private final String surfix;
    private final List<File> files = new ArrayList<File>();

    public FindFileTravoler(String surfix) {
        this.surfix = surfix;
    }

    @Override
    protected boolean processDir(File dir) {
        return true;
    }

    @Override
    protected void processFile(File file) {
        if (file.getName().endsWith(surfix))
        {
            files.add(file);
        }
    }

    public List<File> getFiles() {
        return files;
    }

}
