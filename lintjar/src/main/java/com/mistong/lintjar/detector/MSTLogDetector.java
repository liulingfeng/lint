package com.mistong.lintjar.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.mistong.lintjar.LintConfig;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * 使用封装的Logger打印工具
 * 禁止使用Log
 */
public class MSTLogDetector extends Detector implements Detector.JavaPsiScanner{
    public static final Issue ISSUE = Issue.create(
            "LogUtilsNotUsed",
            "避免使用Log",
            "使用Ln，防止在正式包打印log",
            Category.SECURITY, 7, Severity.ERROR,
            new Implementation(MSTLogDetector.class, Scope.JAVA_FILE_SCOPE));

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
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v", "d", "i", "w", "e", "wtf");
    }

    @Override
    public List<String> getApplicableCallNames() {
        return Arrays.asList("v", "d", "i", "w", "e", "wtf");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
            String msg = lintConfig.getConfig("log-usage-message");
            if("".equals(msg)){
                context.report(ISSUE, call, context.getLocation(call.getMethodExpression()), "请勿使用android.util.Log，建议使用LogUtil工具类");
            }else {
                context.report(ISSUE, call, context.getLocation(call.getMethodExpression()), msg);
            }

        }
    }
}
