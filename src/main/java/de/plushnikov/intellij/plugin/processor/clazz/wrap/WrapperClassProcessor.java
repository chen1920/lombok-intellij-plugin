package de.plushnikov.intellij.plugin.processor.clazz.wrap;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.passiontec.annotation.Wrap;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.clazz.AbstractClassProcessor;
import de.plushnikov.intellij.plugin.processor.handler.WrapperHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Inspect and validate @Wrap annotation on a class
 * Creates wrap class
 *
 * @author chen
 */
public class WrapperClassProcessor extends AbstractClassProcessor {

  private final WrapperHandler wrapperHandler;

  public WrapperClassProcessor(@NotNull WrapperHandler wrapperHandler) {
    super(PsiClass.class, Wrap.class);
    this.wrapperHandler = wrapperHandler;
  }

  @Override
  public boolean isEnabled(@NotNull PropertiesComponent propertiesComponent) {
    return true;
  }

  @Override
  protected boolean validate(@NotNull PsiAnnotation psiAnnotation, @NotNull PsiClass psiClass, @NotNull ProblemBuilder builder) {
    return wrapperHandler.validate(psiClass, psiAnnotation, builder);
  }

  protected void generatePsiElements(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation, @NotNull List<? super PsiElement> target) { if (wrapperHandler.notExistInnerClass(psiClass, psiAnnotation)) {
      target.add(wrapperHandler.createWrapperClass(psiClass, psiAnnotation));
    }
  }
}
