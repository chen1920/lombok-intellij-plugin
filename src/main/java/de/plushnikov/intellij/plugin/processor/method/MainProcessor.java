package de.plushnikov.intellij.plugin.processor.method;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.passiontec.annotation.Main;
import de.plushnikov.intellij.plugin.lombokconfig.ConfigDiscovery;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.LombokPsiElementUsage;
import de.plushnikov.intellij.plugin.psi.LombokLightMethodBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author chen
 */
public class MainProcessor extends AbstractMethodProcessor {


  private final Map<PsiClass, LombokLightMethodBuilder> map = new WeakHashMap();

  public MainProcessor() {
    super(PsiMethod.class, Main.class);
  }


  @Override
  public LombokPsiElementUsage checkFieldUsage(@NotNull PsiField psiField, @NotNull PsiAnnotation psiAnnotation) {
    return LombokPsiElementUsage.READ_WRITE;
  }

  @Override
  protected boolean validate(@NotNull PsiAnnotation psiAnnotation, @NotNull PsiMethod psiMethod, @NotNull ProblemBuilder builder) {
    return true;
  }

  @Override
  protected void processIntern(PsiMethod psiMethod, PsiAnnotation psiAnnotation, List<? super PsiElement> target) {
    final PsiClass psiClass = psiMethod.getContainingClass();
    if (null != psiClass) {
      LombokLightMethodBuilder old = map.get(psiClass);
      if (old == null || target.isEmpty()) {
        String methodName = "main";
        final Project project = psiClass.getProject();
        final PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
        final PsiType psiType = psiElementFactory.createTypeFromText("String[]", psiClass);
        LombokLightMethodBuilder methodBuilder = new LombokLightMethodBuilder(psiClass.getManager(), methodName)
          .withMethodReturnType(PsiType.VOID)
          .withParameter("args", psiType)
          .withContainingClass(psiClass)
          .withNavigationElement(psiAnnotation);
        methodBuilder.withModifier(PsiModifier.PUBLIC).withModifier(PsiModifier.STATIC);
        old = methodBuilder;
        map.put(psiClass, methodBuilder);
      }
      if (!target.contains(old)) {
        target.add(old);
      }
    }
  }
}


