package de.plushnikov.intellij.plugin.processor.handler;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.PsiTypeParameterListOwner;
import com.passiontec.annotation.Wrap;
import de.plushnikov.intellij.plugin.problem.ProblemBuilder;
import de.plushnikov.intellij.plugin.processor.clazz.constructor.NoArgsConstructorProcessor;
import de.plushnikov.intellij.plugin.psi.LombokLightClassBuilder;
import de.plushnikov.intellij.plugin.psi.LombokLightMethodBuilder;
import de.plushnikov.intellij.plugin.util.PsiAnnotationUtil;
import de.plushnikov.intellij.plugin.util.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Handler methods for Wrapper-processing
 *
 * @author chen
 */
public class WrapperHandler {
  private static final String ANNOTATION_WRAPPER_CLASS_NAME = "className";
  private static final String ANNOTATION_PARENT_CLASS_NAME = "parentClass";
  private final NoArgsConstructorProcessor noArgsConstructorProcessor;

  public WrapperHandler(@NotNull NoArgsConstructorProcessor noArgsConstructorProcessor) {
    this.noArgsConstructorProcessor = noArgsConstructorProcessor;
  }

  public boolean validate(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation, @NotNull ProblemBuilder problemBuilder) {
    boolean result = validateAnnotationOnRightType(psiClass, problemBuilder);
    if (result) {
      final String wrapperClassName = getWrapperClassName(psiClass, psiAnnotation);
      result = validateWrapperClassName(wrapperClassName, psiAnnotation.getProject(), problemBuilder);
    }
    return result;
  }

  private boolean validateWrapperClassName(@NotNull String wrapperClassName, @NotNull Project project, @NotNull ProblemBuilder builder) {
    final PsiNameHelper psiNameHelper = PsiNameHelper.getInstance(project);
    if (!psiNameHelper.isIdentifier(wrapperClassName)) {
      builder.addError("%s is not a valid identifier", wrapperClassName);
      return false;
    }
    return true;
  }


  private boolean validateAnnotationOnRightType(@NotNull PsiClass psiClass, @NotNull ProblemBuilder builder) {
    if (psiClass.isAnnotationType() || psiClass.isEnum()) {
      builder.addError(String.format("@%s can be used on classes and interface only", Wrap.class.getName()));
      return false;
    }
    return true;
  }

  public boolean notExistInnerClass(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation) {
    return notExistInnerClass(psiClass, null, psiAnnotation);
  }

  public boolean notExistInnerClass(@NotNull PsiClass psiClass, @Nullable PsiMethod psiMethod, @NotNull PsiAnnotation psiAnnotation) {
    return !getExistInnerBuilderClass(psiClass, psiMethod, psiAnnotation).isPresent();
  }

  public Optional<PsiClass> getExistInnerBuilderClass(@NotNull PsiClass psiClass, @Nullable PsiMethod psiMethod, @NotNull PsiAnnotation psiAnnotation) {
    final String wrapperClassName = getWrapperClassName(psiClass, psiAnnotation, psiMethod);
    return PsiClassUtil.getInnerClassInternByName(psiClass, wrapperClassName);
  }

  @NotNull
  private String getWrapperClassName(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation) {
    return getWrapperClassName(psiClass, psiAnnotation, null);
  }

  @NotNull
  public String getWrapperClassName(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation, @Nullable PsiMethod psiMethod) {
    final String wrapperClassName = PsiAnnotationUtil.getStringAnnotationValue(psiAnnotation, ANNOTATION_WRAPPER_CLASS_NAME);
    if (!StringUtil.isEmptyOrSpaces(wrapperClassName)) {
      return wrapperClassName;
    }
    return "Wrapper";
  }

  @NotNull
  public PsiClass createWrapperClass(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation) {
    LombokLightClassBuilder wrapperClass = createEmptyWrapperClass(psiClass, psiAnnotation);
    wrapperClass.withMethods(createConstructors(wrapperClass, psiAnnotation));
    List<PsiMethod> methods = new ArrayList<>();
    PsiMethod[] classMethods = psiClass.getAllMethods();
    for (PsiMethod classMethod : classMethods) {
      if(classMethod.isConstructor()){
        continue;
      }
      methods.add(createWrapMethod(psiClass, classMethod, wrapperClass));
    }
    wrapperClass.withMethods(methods);
    return wrapperClass;
  }

  @NotNull
  public PsiMethod createWrapMethod(@NotNull PsiClass parentClass, @Nullable PsiMethod psiMethod,
                                    @NotNull PsiClass wrapperClass) {

    final LombokLightMethodBuilder methodBuilder =
      new LombokLightMethodBuilder(parentClass.getManager(), psiMethod.getName())
      .withMethodReturnType(psiMethod.getReturnType())
      .withContainingClass(wrapperClass)
      .withNavigationElement(parentClass)
      .withModifier(PsiModifier.PUBLIC);
    return methodBuilder;
  }

  @NotNull
  private LombokLightClassBuilder createEmptyWrapperClass(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation) {
    return createWrapperClass(psiClass, psiClass, true, psiAnnotation);
  }

  @NotNull
  private LombokLightClassBuilder createWrapperClass(@NotNull PsiClass psiClass, @NotNull PsiTypeParameterListOwner psiTypeParameterListOwner, final boolean isStatic, @NotNull PsiAnnotation psiAnnotation) {
    PsiMethod psiMethod = null;
    if (psiTypeParameterListOwner instanceof PsiMethod) {
      psiMethod = (PsiMethod) psiTypeParameterListOwner;
    }

    final String wrapperClassName = getWrapperClassName(psiClass, psiAnnotation, psiMethod);
    final String wrapperClassQualifiedName = psiClass.getQualifiedName() + "." + wrapperClassName;

    final LombokLightClassBuilder classBuilder = new LombokLightClassBuilder(psiClass, wrapperClassName, wrapperClassQualifiedName)
      .withContainingClass(psiClass)
      .withNavigationElement(psiAnnotation)
      .withParameterTypes((null != psiMethod && psiMethod.isConstructor()) ? psiClass.getTypeParameterList() : psiTypeParameterListOwner.getTypeParameterList())
      .withModifier(PsiModifier.PUBLIC);
    if (isStatic) {
      classBuilder.withModifier(PsiModifier.STATIC);
    }
    String parentClass = PsiAnnotationUtil.getStringAnnotationValue(psiAnnotation, ANNOTATION_PARENT_CLASS_NAME);
    if (parentClass == null) {
      parentClass = "com.chen.module.ModuleWrap";
    }
    if (!parentClass.isEmpty()) {
      final Project project = psiClass.getProject();
      final PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
      final PsiClassType psiType = psiElementFactory.createTypeByFQClassName(parentClass);
      classBuilder.getExtendsList().addReference(psiType);
    }
    if (psiClass.isInterface()) {
      classBuilder.getImplementsList().addReference(psiClass);
    }
    return classBuilder;
  }

  @NotNull
  public Collection<PsiMethod> createConstructors(@NotNull PsiClass psiClass, @NotNull PsiAnnotation psiAnnotation) {
    final Collection<PsiMethod> methodsIntern = PsiClassUtil.collectClassConstructorIntern(psiClass);

    final String constructorName = noArgsConstructorProcessor.getConstructorName(psiClass);
    for (PsiMethod existedConstructor : methodsIntern) {
      if (constructorName.equals(existedConstructor.getName()) && existedConstructor.getParameterList().getParametersCount() == 0) {
        return Collections.emptySet();
      }
    }
    return noArgsConstructorProcessor.createNoArgsConstructor(psiClass, PsiModifier.PUBLIC, psiAnnotation);
  }

}
