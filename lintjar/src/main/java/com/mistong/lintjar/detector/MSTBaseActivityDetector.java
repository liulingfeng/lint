package com.mistong.lintjar.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReferenceList;

import java.util.Collections;
import java.util.List;

/**
 * 所有Activity继承BasePresenterActivity，避免遗漏和出错
 */
public class MSTBaseActivityDetector extends Detector implements Detector.JavaPsiScanner {
    public static Issue ISSUE = Issue.create("BaseActivityExtends",
            "继承BasePresenterActivity",
            "Activity需要继承BasePresenterActivity",
            Category.CORRECTNESS, 5, Severity.ERROR,
            new Implementation(MSTBaseActivityDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.singletonList(PsiClass.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new ShowFinder(context);
    }

    private static class ShowFinder extends JavaElementVisitor {
        private boolean isFound;
        private JavaContext context;

        public ShowFinder(JavaContext context) {
            this.context = context;
        }

        @Override
        public void visitClass(PsiClass aClass) {
            String className = aClass.getName();

            if (className == null) {
                return;
            }
            if (!className.endsWith("Activity") || className.endsWith("BasePresenterActivity")) {
                isFound = false;
                return;
            }
            PsiReferenceList referenceList = aClass.getExtendsList();
            if (referenceList != null) {
                PsiJavaCodeReferenceElement[] elements = referenceList.getReferenceElements();
                for (PsiJavaCodeReferenceElement element : elements) {
                    String name = element.getReferenceName();
                    if (name != null)
                        isFound = name.equals("BasePresenterActivity");
                }
                if (!isFound) {
                    context.report(ISSUE, context.getLocation(aClass), "是activity但是没有继承BasePresenterActivity");
                }
            }

        }
    }
}
