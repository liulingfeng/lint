package com.mistong.lintjar.detector;

import com.android.resources.ResourceType;
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLiteral;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiReturnStatement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class One extends Detector implements Detector.JavaPsiScanner {
    public static final Issue ISSUE = Issue.create(
            "ShowToastNew",
            "Toast created but not shown",

            "`Toast.makeText()` creates a `Toast` but does *not* show it. You must call " +
                    "`show()` on the resulting object to actually make the `Toast` appear.",

            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            new Implementation(
                    One.class,
                    Scope.JAVA_FILE_SCOPE));

    //返回需要检查的Psi元素类型列表
    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.singletonList(PsiNewExpression.class);
    }

    //getApplicablePsiTypes返回到这里
    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return super.createPsiVisitor(context);
    }

    //捕获方法，返回方法名的列表
    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("makeText");
    }

    //分析方法
    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        if (!(context.getEvaluator().isMemberInClass(method, "android.widget.Toast"))) {
            return;
        }

        //psi树
        //PsiTreeUtil.getParentOfType 获取父方法 得到父类
        //PsiLiteral 参数是一个变量，自定义的
        PsiExpression[] args = call.getArgumentList().getExpressions();
        if (args.length == 3) {
            PsiExpression duration = args[2];
            if (duration instanceof PsiLiteral) {
                context.report(ISSUE, duration, context.getLocation(duration),
                        "Expected duration `Toast.LENGTH_SHORT` or `Toast.LENGTH_LONG`, a custom " +
                                "duration value is not supported");
            }
        }

        //获取上一层的方法
        PsiMethod surroundingMethod = PsiTreeUtil.getParentOfType(call, PsiMethod.class, true);
        if (surroundingMethod == null) {
            return;
        }

        System.out.println(surroundingMethod.getName()+surroundingMethod.getText());
        ShowFinder finder = new ShowFinder(call);
        surroundingMethod.accept(finder);
        if (!finder.isShowCalled()) {
            context.report(ISSUE, call, context.getLocation(call.getMethodExpression()),
                    "Toast created but not shown: did you forget to call `show()` ?");
        }
    }

    private static class ShowFinder extends JavaRecursiveElementVisitor {
        private final PsiMethodCallExpression mTarget;
        private boolean mFound;
        /**
         * Whether we've seen the target makeText node yet
         */
        private boolean mSeenTarget;  //需要监控的对象

        private ShowFinder(PsiMethodCallExpression target) {
            mTarget = target;
        }

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
            super.visitMethodCallExpression(expression);

            if (expression == mTarget) {
                System.out.println("初始化调用");
                mSeenTarget = true;
            } else {
                System.out.println("第二次调用");
                System.out.println(mTarget.getText());
                PsiReferenceExpression methodExpression = expression.getMethodExpression();
                System.out.println(methodExpression.getQualifiedName());
                System.out.println(methodExpression.getReferenceName());
                if ((mSeenTarget || methodExpression.getQualifier() == mTarget)
                        && "show".equals(methodExpression.getReferenceName())) {
                    // TODO: Do more flow analysis to see whether we're really calling show
                    // on the right type of object?
                    mFound = true;
                }
            }
        }

        @Override
        public void visitReturnStatement(PsiReturnStatement statement) {
            super.visitReturnStatement(statement);

            if (statement.getReturnValue() == mTarget) {
                // If you just do "return Toast.makeText(...) don't warn
                mFound = true;
            }
        }

        boolean isShowCalled() {
            return mFound;
        }
    }

    @Override
    public List<String> getApplicableReferenceNames() {
        return super.getApplicableReferenceNames();
    }

    //返回类名的列表
    @Override
    public List<String> getApplicableConstructorTypes() {
        return super.getApplicableConstructorTypes();
    }

    //getApplicableConstructorTypes返回到这里
    @Override
    public void visitConstructor(JavaContext context, JavaElementVisitor visitor, PsiNewExpression node, PsiMethod constructor) {
        super.visitConstructor(context, visitor, node, constructor);
    }

    @Override
    public Collection<String> getApplicableElements() {
        return super.getApplicableElements();
    }

    @Override
    public Collection<String> getApplicableAttributes() {
        return super.getApplicableAttributes();
    }

    @Override
    public List<String> getApplicableCallOwners() {
        return super.getApplicableCallOwners();
    }

    @Override
    public List<String> getApplicableCallNames() {
        return super.getApplicableCallNames();
    }

    //返回true时，java代码中的资源引用R.layout.main会被检查
    @Override
    public boolean appliesToResourceRefs() {
        return true;
    }

    @Override
    public void visitResourceReference(JavaContext context, JavaElementVisitor visitor, PsiElement node, ResourceType type, String name, boolean isFramework) {
        super.visitResourceReference(context, visitor, node, type, name, isFramework);
    }

    //返回父类名的列表
    @Override
    public List<String> applicableSuperClasses() {
        return super.applicableSuperClasses();
    }

    @Override
    public void checkClass(JavaContext context, PsiClass declaration) {
        super.checkClass(context, declaration);
    }
}
