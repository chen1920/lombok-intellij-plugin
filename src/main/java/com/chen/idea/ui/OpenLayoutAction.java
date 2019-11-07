package com.chen.idea.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author chen
 */
public class OpenLayoutAction extends AnAction {

    private static final String TAG = "OpenLayoutAction";

    /**
     * Implement this method to provide your action handler.
     *
     * @param event Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (file == null || file.isDirectory() || !file.getName().endsWith(Constants.FILE_EXT)) {
            return;
        }
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        String xmlPath = file.getPath();
        String err = DesignerClient.openLayout(xmlPath);
        if (err != null) {
            System.out.println("openFail err = " + err);
        }

    }
}
