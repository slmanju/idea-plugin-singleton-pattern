package com.slmanju.singletonpattern;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;

/**
 * @author Manjula Jayawardana
 */
public class LazySingletonGenerator {

  private PsiClass targetClass;
  private PsiElementFactory elementFactory;
  private String className;

  public LazySingletonGenerator(PsiClass targetClass) {
    this.targetClass = targetClass;
    this.elementFactory = JavaPsiFacade.getElementFactory(targetClass.getProject());
    this.className = targetClass.getName();
  }

  public static LazySingletonGenerator getInstance(PsiClass targetClass) {
    return new LazySingletonGenerator(targetClass);
  }

  public void generate() {
    targetClass.add(createInnerHolder());
    targetClass.add(createInstanceMethod());
  }

  private PsiClass createInnerHolder() {
    PsiClass lazyHolder = elementFactory.createClass("LazyHolder");
    lazyHolder.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
    lazyHolder.getModifierList().setModifierProperty(PsiModifier.STATIC, true);
    lazyHolder.getModifierList().setModifierProperty(PsiModifier.FINAL, true);

    String stringInstance = "private static final " + className + " INSTANCE = new " + className + "();";
    PsiField singletonInstance = elementFactory.createFieldFromText(stringInstance, null);
    lazyHolder.add(singletonInstance);

    return lazyHolder;
  }

  private PsiMethod createInstanceMethod() {
    String stringInstanceMethod = "public static " + className + " getInstance() {" +
        "return LazyHolder.INSTANCE;" +
        "}";
    return elementFactory.createMethodFromText(stringInstanceMethod, null);
  }

}
