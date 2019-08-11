package de.plushnikov.intellij.plugin.processor.clazz;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import de.plushnikov.intellij.plugin.psi.LombokLightFieldBuilder;
import de.plushnikov.intellij.plugin.util.PsiClassUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author chen
 */
public class ReaderProcessor {


  protected void generatePsiElements(@NotNull PsiClass psiClass, @NotNull PsiAnnotation
    psiAnnotation, @NotNull List<? super PsiElement> target, String name) {
    LombokLightFieldBuilder field = (createReaderField(psiClass, psiAnnotation, name));
    target.add(field);
  }

  private LombokLightFieldBuilder createReaderField(@NotNull PsiClass psiClass,
                                                    @NotNull PsiAnnotation psiAnnotation, String name) {

    final Project project = psiClass.getProject();
    final PsiManager manager = psiClass.getContainingFile().getManager();
    final PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
    String typeName = psiClass.getQualifiedName();
    final PsiType psiReaderType = psiElementFactory.createTypeFromText(typeName, psiClass);
    LombokLightFieldBuilder field = new LombokLightFieldBuilder(manager, name, psiReaderType)
      .withContainingClass(psiClass)
      .withModifier(PsiModifier.FINAL)
      .withModifier(PsiModifier.PUBLIC)
      .withModifier(PsiModifier.STATIC)
      .withNavigationElement(psiAnnotation);
    final PsiExpression initializer = psiElementFactory.createExpressionFromText("new " + typeName + "()", psiClass);
    field.setInitializer(initializer);

    return field;
  }


  protected boolean hasFieldByName(@NotNull PsiClass psiClass, String... fieldNames) {
    final Collection<PsiField> psiFields = PsiClassUtil.collectClassFieldsIntern(psiClass);
    for (PsiField psiField : psiFields) {
      for (String fieldName : fieldNames) {
        if (fieldName.equals(psiField.getName())) {
          return true;
        }
      }
    }
    return false;
  }

  public void generateTag(PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation,
                          List<? super PsiElement> target, String tagName) {
    final Project project = psiClass.getProject();
    final PsiManager manager = psiClass.getContainingFile().getManager();
    final PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
    final PsiType tagType = psiElementFactory.createTypeFromText("String", psiClass);

    LombokLightFieldBuilder field = new LombokLightFieldBuilder(manager, tagName, tagType)
      .withContainingClass(psiClass)
      .withModifier(PsiModifier.FINAL)
      .withModifier(PsiModifier.PRIVATE)
      .withModifier(PsiModifier.STATIC)
      .withNavigationElement(psiAnnotation);
    final PsiExpression initializer = psiElementFactory.createExpressionFromText("\"" + psiClass.getName() + "\"", psiClass);
    field.setInitializer(initializer);
    target.add(field);
  }
}
