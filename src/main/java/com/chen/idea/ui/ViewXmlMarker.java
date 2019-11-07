package com.chen.idea.ui;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTokenImpl;
import com.intellij.psi.tree.xml.IXmlLeafElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

/**
 * @author chen
 */
public class ViewXmlMarker extends LineMarkerProviderDescriptor
        implements GutterIconNavigationHandler<PsiElement> {

    private static final String TAG = "RcXmlMarker";
    private static final Icon navigationOnIcon;
    private static final String VIEW = "view";

    static {
        navigationOnIcon = IconLoader.getIcon("/icon/layout.png");
    }

    /**
     * Human-readable provider name for UI.
     *
     * @return null if no configuration needed
     */
    @Nullable("null means disabled")
    @Override
    public String getName() {
        return "Layout xml Location";
    }

    @Override
    public void navigate(MouseEvent e, PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        DesignerClient.openLayout(file.getPath());
    }

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (isLayoutXml(element)) {
            return new LineMarkerInfo<PsiElement>(element, element.getTextRange(), navigationOnIcon,
                    Pass.UPDATE_ALL, null, this,
                    GutterIconRenderer.Alignment.LEFT);
        }
        return null;
    }

    private boolean isLayoutXml(PsiElement element) {
        if (element instanceof XmlTokenImpl) {
            XmlTokenImpl ref = (XmlTokenImpl) element;
            if (!(ref.getTokenType() instanceof IXmlLeafElementType)) {
                return false;
            }
            String text = ref.getText();
            if (VIEW.equals(text)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
