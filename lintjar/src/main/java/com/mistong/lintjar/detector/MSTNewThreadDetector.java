package com.mistong.lintjar.detector;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNewExpression;
import com.mistong.lintjar.LintConfig;

import java.util.Collections;
import java.util.List;

/**
 * 禁止直接创建线程
 */
public class MSTNewThreadDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create(
            "NewThread",
            "避免自己创建Thread",
            "请勿直接调用new Thread()，建议使用AsyncTask或统一的线程管理工具类",
            Category.PERFORMANCE, 5, Severity.WARNING,
            new Implementation(MSTNewThreadDetector.class, Scope.JAVA_FILE_SCOPE));

    private LintConfig lintConfig;

    @Override
    public void beforeCheckProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public void beforeCheckLibraryProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Collections.singletonList("java.lang.Thread");
    }

    @Override
    public void visitConstructor(@NonNull JavaContext context, @com.android.annotations.Nullable JavaElementVisitor visitor,
                                 @NonNull PsiNewExpression node, @NonNull PsiMethod constructor) {
        String msg = lintConfig.getConfig("new-thread-message");
        context.report(ISSUE, node, context.getLocation(node), msg);
    }
}
