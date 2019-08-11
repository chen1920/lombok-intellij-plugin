package de.plushnikov.intellij.plugin.processor.clazz;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.passiontec.annotation.Model;
import de.plushnikov.intellij.plugin.lombokconfig.ConfigDiscovery;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.LombokPsiElementUsage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Plushnikov Michail
 */
public class ModelFieldProcessor extends AbstractClassProcessor {

  private final ReaderProcessor readerProcessor;

  public ModelFieldProcessor(@NotNull ConfigDiscovery configDiscovery,
                             @NotNull GetterProcessor getterProcessor, @NotNull SetterProcessor setterProcessor) {
    super(configDiscovery, PsiField.class, Model.class);
    this.readerProcessor = new ReaderProcessor();
  }

  @Override
  protected boolean validate(@NotNull PsiAnnotation psiAnnotation, @NotNull PsiClass psiClass, @NotNull ProblemBuilder builder) {
    return validateAnnotationOnRightType(psiClass, builder);
  }

  private boolean validateAnnotationOnRightType(@NotNull PsiClass psiClass, @NotNull ProblemBuilder builder) {
    boolean result = true;
    if (psiClass.isAnnotationType() || psiClass.isInterface() || psiClass.isEnum()) {
      builder.addError("'@Data' is only supported on a class type");
      result = false;
    }
    return result;
  }

  @Override
  protected void generatePsiElements(@NotNull PsiClass psiClass,
                                     @NotNull PsiAnnotation psiAnnotation,
                                     @NotNull List<? super PsiElement> target) {
    String readerName = "READER";
    if (!readerProcessor.hasFieldByName(psiClass, readerName)) {
      readerProcessor.generatePsiElements(psiClass, psiAnnotation, target, readerName);
    }
    String tagName = "TAG";
    if (!readerProcessor.hasFieldByName(psiClass, tagName)) {
      readerProcessor.generateTag(psiClass, psiAnnotation, target, tagName);
    }
  }

  @Override
  public LombokPsiElementUsage checkFieldUsage(@NotNull PsiField psiField, @NotNull PsiAnnotation psiAnnotation) {
    return LombokPsiElementUsage.READ_WRITE;
  }
}
