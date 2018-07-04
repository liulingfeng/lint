package com.mistong.lintjar.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Collections;
import java.util.List;

/**
 * 广播是否成对出现
 */
public class MSTBroadcastDetector extends Detector implements Detector.JavaPsiScanner {
    public static Issue ISSUE = Issue.create("BroadcastTwin",
            "广播动态注册的情况下onDestroy需要反注册",
            "广播反注册",
            Category.CORRECTNESS, 5, Severity.ERROR,
            new Implementation(MSTBroadcastDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("registerReceiver");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, "android.content.ContextWrapper")) {
            PsiClass psiClass = PsiTreeUtil.getParentOfType(call, PsiClass.class, true);
            if (psiClass != null) {
                PsiMethod[] methods = psiClass.findMethodsByName("onDestroy", true);
                for (PsiMethod methodChild : methods) {
                    if (methodChild.getName().equals("onDestroy")) {
                        ShowFinder finder = new ShowFinder();
                        //遍历内部所有方法
                        methodChild.accept(finder);

                        if (!finder.isUnregisterCalled()) {
                            context.report(ISSUE, call, context.getLocation(call.getMethodExpression()), "在onDestroy反注册一下");
                        } else {
                            super.visitMethod(context, visitor, call, method);
                        }
                    }
                }
            }

        }
    }

    private static class ShowFinder extends JavaRecursiveElementVisitor {
        private boolean isFound;

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
            PsiReferenceExpression referenceExpression = expression.getMethodExpression();
            if ("unregisterReceiver".equals(referenceExpression.getReferenceName())) {
                isFound = true;
            }
            super.visitMethodCallExpression(expression);
        }

        boolean isUnregisterCalled() {
            return isFound;
        }
    }
}
