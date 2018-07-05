package com.mistong.lintjar.detector;

import com.android.tools.lint.checks.HandlerDetector;
import com.android.tools.lint.checks.OverrideConcreteDetector;
import com.android.tools.lint.checks.OverrideDetector;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeCastExpression;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collections;
import java.util.List;

/**
 * 序列化的对象实现Serializable接口
 */

public class MSTSerializableDetector extends Detector implements Detector.JavaPsiScanner {
    public static Issue ISSUE = Issue.create("Serializable",
            "没有实现Serializable",
            "实现Serializable",
            Category.CORRECTNESS, 5, Severity.ERROR,
            new Implementation(MSTSerializableDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("getSerializableExtra");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
//        PsiType psiType = method.getReturnType();
//        if (psiType != null) {
//            System.out.println(psiType.toString() + "德瑪西亞");
//        }
        PsiTypeCastExpression expression = PsiTreeUtil.getParentOfType(call, PsiTypeCastExpression.class, true);
        if (expression != null) {
            System.out.println(expression.getContext().getText());
        }
//        if (psiClass != null) {
//            PsiModifierList modifierList = psiClass.getModifierList();
//            if (modifierList == null) {
//                return;
//            }
//            System.out.println("德瑪西亞");
//            if (!"@Keep".equals(modifierList.getText())) {
//                context.report(ISSUE, context.getLocation(call.getMethodExpression()), "没有实现o");
//            }
//        }
        super.visitMethod(context, visitor, call, method);
    }
}
