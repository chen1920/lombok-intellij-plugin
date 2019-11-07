package com.chen.idea.ui;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.impl.source.PsiJavaCodeReferenceElementImpl;
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.impl.source.tree.java.PsiReferenceParameterListImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * @author chen
 */
public class RcXmlMarker extends LineMarkerProviderDescriptor
        implements GutterIconNavigationHandler<PsiElement> {

    private static final String TAG = "RcXmlMarker";
    private static final Icon navigationOnIcon;
    private static final String LAYOUT_START = "Rc.layout.";
    private static final String UI_WEB_START = "UiWeb";

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
        if (element instanceof PsiReferenceExpression) {
            PsiReferenceExpression ref = (PsiReferenceExpression) element;
            String caInfo = ref.getCanonicalText();
            if (caInfo.startsWith(LAYOUT_START)) {
                String path = caInfo.substring(LAYOUT_START.length());
                String basePath = element.getProject().getBasePath();
                String xmlPath = FindTool.findLayoutXml(basePath, path);
                if (xmlPath != null) {
                    String err = DesignerClient.openLayout(xmlPath);
                    if (err != null) {
                        System.out.println("openFail err = " + err);
                    }
                }
            }
        } else if (isUiWeb(element)) {
            try {
                openUiWeb(element.getParent());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void openUiWeb(PsiElement parent) throws Exception {
        if (parent instanceof PsiJavaCodeReferenceElementImpl) {
            PsiElement first = parent.getNextSibling().getFirstChild();
            PsiElement last = first.getNextSibling().getLastChild().getLastChild();
            if (last instanceof PsiJavaTokenImpl) {
                String url = last.getText();
                url = url.substring(1, url.length() - 1);
                java.awt.Desktop.getDesktop().browse(new URI(url));
            }
        }
    }


    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (isLayoutXml(element)) {
            return new LineMarkerInfo<PsiElement>(element, element.getTextRange(), navigationOnIcon,
                    Pass.UPDATE_ALL, null, this,
                    GutterIconRenderer.Alignment.LEFT);
        }
        if (isUiWeb(element)) {
            return new LineMarkerInfo<PsiElement>(element, element.getTextRange(), navigationOnIcon,
                    Pass.UPDATE_ALL, null, this,
                    GutterIconRenderer.Alignment.LEFT);
        }
        return null;
    }

    private boolean isLayoutXml(PsiElement element) {
        if (element instanceof PsiReferenceExpression) {
            PsiReferenceExpression ref = (PsiReferenceExpression) element;
            String caInfo = ref.getCanonicalText();
            if (caInfo.startsWith(LAYOUT_START)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUiWeb(PsiElement element) {
        if (element instanceof PsiIdentifierImpl) {
            PsiIdentifierImpl ref = (PsiIdentifierImpl) element;
            if (ref.getPrevSibling() != null || ref.getNextSibling() == null) {
                return false;
            }
            String caInfo = ref.getText();
            if (!caInfo.equals(UI_WEB_START)) {
                return false;
            }
            PsiElement next = ref.getNextSibling();
            return next instanceof PsiReferenceParameterListImpl;
        }
        return false;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }
}
