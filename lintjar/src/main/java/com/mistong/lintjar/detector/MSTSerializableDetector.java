package com.mistong.lintjar.detector;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
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
    public static Issue ISSUE = Issue.create("EntitySerializable",
            "实体类没有实现序列化",
            "序列化的实体对象没有实现序列化，混淆会出错",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(MSTSerializableDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("getSerializableExtra");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        if (!evaluator.isMemberInClass(method, "android.content.Intent")) {
            return;
        }

        PsiTypeCastExpression psiTypeCastExpression = PsiTreeUtil.getParentOfType(call, PsiTypeCastExpression.class);
        if (psiTypeCastExpression == null) {
            return;
        }
        PsiTypeElement psiTypeElement = psiTypeCastExpression.getCastType();
        if (psiTypeElement != null) {
            PsiType psiType = psiTypeElement.getType();
            if (psiType instanceof PsiClassType) {
                PsiClassType classType = (PsiClassType) psiType;
                PsiClass cls = classType.resolve();
                if (cls == null) {
                    return;
                }
                if (isSerializableEntity(cls)) {
                    PsiClass[] classes = cls.getInnerClasses();
                    for (PsiClass ps : classes) {
                        if (!isSerializableEntity(ps)) {
                            context.report(ISSUE, context.getLocation(call.getMethodExpression()), "序列化的对象以及内部类必须实现Serializable或者Parcelable，并且加上@Keep标记");
                        }
                    }
                } else {
                    context.report(ISSUE, context.getLocation(call.getMethodExpression()), "序列化的对象以及内部类必须实现Serializable或者Parcelable，并且加上@Keep标记");
                }
            }
        }
    }

    private static boolean isSerializableEntity(PsiClass clz) {
        while (clz != null) {
            PsiModifierList modifierList = clz.getModifierList();

            if (modifierList == null) {
                return false;
            }

            PsiClassType[] types = clz.getImplementsListTypes();
            for (PsiClassType classType : types) {
                String name = classType.getClassName();
                if (name != null)
                    if (modifierList.findAnnotation("android.support.annotation.Keep") != null && ("Serializable".equals(name) || "Parcelable".equals(name))) {
                        return true;
                    }
            }
            clz = clz.getSuperClass();
        }

        return false;
    }
}
