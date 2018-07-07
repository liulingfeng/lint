package com.mistong.lintjar.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiTryStatement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 检查所有的parse方法
 * 防止类型转换出错
 */
public class MSTParseDetector extends Detector implements Detector.JavaPsiScanner {
    public static Issue ISSUE = Issue.create("ParseStandard",
            "parse方法使用规范",
            "parse方法需要加try catch",
            Category.SECURITY, 6, Severity.ERROR,
            new Implementation(MSTParseDetector.class, Scope.JAVA_FILE_SCOPE)).addMoreInfo("https://github.com/liulingfeng/lint");

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("parseColor", "parseDouble", "parseInt", "parseFloat");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        boolean isColor = context.getEvaluator().isMemberInClass(method, "android.graphics.Color");
        boolean isInteger = context.getEvaluator().isMemberInClass(method, "java.lang.Integer");
        boolean isDouble = context.getEvaluator().isMemberInClass(method, "java.lang.Double");
        boolean isFloat = context.getEvaluator().isMemberInClass(method, "java.lang.Float");

        //过滤自定义的parse方法
        if (!isColor && !isInteger && !isDouble && !isFloat) {
            return;
        }
        PsiTryStatement statement = PsiTreeUtil.getParentOfType(call, PsiTryStatement.class, true);
        if (statement == null) {
            context.report(ISSUE, call, context.getLocation(call.getMethodExpression()), "parse方法没有加try catch");
        } else {
            super.visitMethod(context, visitor, call, method);
        }
    }
}
