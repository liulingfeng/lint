package com.mistong.lintjar.detector;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.checks.AddJavascriptInterfaceDetector;
import com.android.tools.lint.checks.AnnotationDetector;
import com.android.tools.lint.checks.FragmentDetector;
import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

import java.util.Arrays;
import java.util.List;

import static com.android.SdkConstants.CLASS_FRAGMENT;
import static com.android.SdkConstants.CLASS_V4_FRAGMENT;

public class Test extends Detector implements Detector.JavaPsiScanner{
    public static final Issue ISSUE = Issue.create(
            "ValidFragment2",
            "Fragment not instantiatable",

            "From the Fragment documentation:\n" +
                    "*Every* fragment must have an empty constructor, so it can be instantiated when " +
                    "restoring its activity's state. It is strongly recommended that subclasses do not " +
                    "have other constructors with parameters, since these constructors will not be " +
                    "called when the fragment is re-instantiated; instead, arguments can be supplied " +
                    "by the caller with `setArguments(Bundle)` and later retrieved by the Fragment " +
                    "with `getArguments()`.",

            Category.CORRECTNESS,
            6,
            Severity.FATAL,
            new Implementation(
                    Test.class,
                    Scope.JAVA_FILE_SCOPE)
    ).addMoreInfo(
            "http://developer.android.com/reference/android/app/Fragment.html#Fragment()");


    /** Constructs a new {@link FragmentDetector} */
    public Test() {
    }

    // ---- Implements JavaScanner ----

    @Nullable
    @Override
    public List<String> applicableSuperClasses() {
        return Arrays.asList(CLASS_FRAGMENT, CLASS_V4_FRAGMENT);
    }

    @Override
    public void checkClass(@NonNull JavaContext context, @NonNull PsiClass node) {
        if (node instanceof PsiAnonymousClass) {
            String message = "Fragments should be static such that they can be re-instantiated by " +
                    "the system, and anonymous classes are not static";
            PsiElement locationNode = JavaContext.findNameElement(node);
            if (locationNode == null) {
                locationNode = node;
            }
            context.report(ISSUE, locationNode, context.getLocation(locationNode), message);
            return;
        }

        JavaEvaluator evaluator = context.getEvaluator();
        if (evaluator.isAbstract(node)) {
            return;
        }

        if (!evaluator.isPublic(node)) {
            String message = String.format("This fragment class should be public (%1$s)",
                    node.getQualifiedName());
            context.report(ISSUE, node, context.getNameLocation(node), message);
            return;
        }

        if (node.getContainingClass() != null && !evaluator.isStatic(node)) {
            String message = String.format(
                    "This fragment inner class should be static (%1$s)", node.getQualifiedName());
            context.report(ISSUE, node, context.getNameLocation(node), message);
            return;
        }

        boolean hasDefaultConstructor = false;
        boolean hasConstructor = false;
        for (PsiMethod constructor : node.getConstructors()) {
            hasConstructor = true;
            if (constructor.getParameterList().getParametersCount() == 0) {
                if (evaluator.isPublic(constructor)) {
                    hasDefaultConstructor = true;
                } else {
                    Location location = context.getNameLocation(constructor);
                    context.report(ISSUE, constructor, location,
                            "The default constructor must be public");
                    // Also mark that we have a constructor so we don't complain again
                    // below since we've already emitted a more specific error related
                    // to the default constructor
                    hasDefaultConstructor = true;
                }
            } else {
                Location location = context.getNameLocation(constructor);
                // TODO: Use separate issue for this which isn't an error
                String message = "Avoid non-default constructors in fragments: "
                        + "use a default constructor plus "
                        + "`Fragment#setArguments(Bundle)` instead";
                context.report(ISSUE, constructor, location, message);
            }
        }

        if (!hasDefaultConstructor && hasConstructor) {
            String message = String.format(
                    "This fragment should provide a default constructor (a public " +
                            "constructor with no arguments) (`%1$s`)",
                    node.getQualifiedName());
            context.report(ISSUE, node, context.getNameLocation(node), message);
        }
    }
}

