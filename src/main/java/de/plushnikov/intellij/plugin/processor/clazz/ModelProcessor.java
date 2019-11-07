package de.plushnikov.intellij.plugin.processor.clazz;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.passiontec.annotation.Model;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.LombokPsiElementUsage;
import de.plushnikov.intellij.plugin.psi.LombokLightMethodBuilder;
import de.plushnikov.intellij.plugin.util.PsiAnnotationSearchUtil;
import de.plushnikov.intellij.plugin.util.PsiAnnotationUtil;
import de.plushnikov.intellij.plugin.util.PsiClassUtil;
import de.plushnikov.intellij.plugin.util.PsiMethodUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * @author chen
 */
public class ModelProcessor extends AbstractClassProcessor {

  private final GetterProcessor getterProcessor;
  private final SetterProcessor setterProcessor;

  public ModelProcessor( @NotNull GetterProcessor getterProcessor, @NotNull SetterProcessor setterProcessor) {
    super(PsiMethod.class, Model.class);
    this.getterProcessor = getterProcessor;
    this.setterProcessor = setterProcessor;
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
    final boolean get = PsiAnnotationUtil.getBooleanAnnotationValue(psiAnnotation, "get", true);
    final boolean set = PsiAnnotationUtil.getBooleanAnnotationValue(psiAnnotation, "set", true);
    final boolean builder = PsiAnnotationUtil.getBooleanAnnotationValue(psiAnnotation, "builder", false);
    if (get && PsiAnnotationSearchUtil.isNotAnnotatedWith(psiClass, Getter.class)) {
      target.addAll(getterProcessor.createFieldGetters(psiClass, PsiModifier.PUBLIC));
    }
    if (set && PsiAnnotationSearchUtil.isNotAnnotatedWith(psiClass, Setter.class)) {
      target.addAll(setterProcessor.createFieldSetters(psiClass, PsiModifier.PUBLIC, builder));
    }
    PsiClass superClass = psiClass.getSuperClass();
    if (superClass != null && "Saveable".equals(superClass.getName())) {
      return;
    }
    Collection<PsiMethod> methods = PsiClassUtil.collectClassMethodsIntern(psiClass);
    boolean haveNewObject = false;
    boolean haveNewArray = false;
    for (PsiMethod method : methods) {
      if (method.getName().equals("newObject")) {
        if (!method.hasParameters()) {
          haveNewObject = true;
        }
      } else if (method.getName().equals("newArray") && method.hasParameters()) {
        if (method.getParameterList().getParametersCount() == 1) {
          haveNewArray = true;
        }
      }
    }
    String classSimpleName = psiClass.getName();
    if (classSimpleName != null && !classSimpleName.isEmpty()) {
      if (!haveNewObject) {
        target.add(createNewObject(psiClass, "public"));
      }
      if (!haveNewArray) {
        target.add(createNewArray(psiClass, "public"));
      }
    }
  }


  @NotNull
  public PsiMethod createNewObject(@NotNull PsiClass psiClass, @NotNull String methodModifier) {
    String classSimpleName = psiClass.getName();
    final String methodName = "newObject";
    LombokLightMethodBuilder methodBuilder = new LombokLightMethodBuilder(psiClass.getManager(), methodName)
      .withMethodReturnType(PsiClassUtil.getTypeWithGenerics(psiClass))
      .withContainingClass(psiClass)
      .withNavigationElement(psiClass);
    if (StringUtil.isNotEmpty(methodModifier)) {
      methodBuilder.withModifier(methodModifier);
    }
    final String blockText = "return new " + classSimpleName + "();";
    methodBuilder.withBody(PsiMethodUtil.createCodeBlockFromText(blockText, methodBuilder));
    return methodBuilder;
  }

  @NotNull
  public PsiMethod createNewArray(@NotNull PsiClass psiClass, @NotNull String methodModifier) {
    String classSimpleName = psiClass.getName();
    final String methodName = "newArray";
    final Project project = psiClass.getProject();
    final PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
    final PsiType psiType = psiElementFactory.createTypeFromText(classSimpleName + "[]", psiClass);
    LombokLightMethodBuilder methodBuilder = new LombokLightMethodBuilder(psiClass.getManager(), methodName)
      .withMethodReturnType(psiType)
      .withContainingClass(psiClass)
      .withNavigationElement(psiClass);
    if (StringUtil.isNotEmpty(methodModifier)) {
      methodBuilder.withModifier(methodModifier);
    }
    methodBuilder.withParameter("size", psiElementFactory.createTypeFromText("int", psiClass));
    final String blockText = "return new " + classSimpleName + "[size];";
    methodBuilder.withBody(PsiMethodUtil.createCodeBlockFromText(blockText, methodBuilder));
    return methodBuilder;
  }

  @Override
  public LombokPsiElementUsage checkFieldUsage(@NotNull PsiField psiField, @NotNull PsiAnnotation psiAnnotation) {
    return LombokPsiElementUsage.READ_WRITE;
  }
}
