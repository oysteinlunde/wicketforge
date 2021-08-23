/*
 * Copyright 2010 The WicketForge-Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicketforge.inspection;

import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;

import wicketforge.Constants;
import icons.WicketForgeIcons;
import wicketforge.psi.references.ClassWicketIdReference;

/**
 */
public class ClassWicketIdInspection extends AbstractBaseJavaLocalInspectionTool {
    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
            }

            @Override
            public void visitExpression(PsiExpression expression) {
                super.visitExpression(expression);
                // ClassWicketIdReference are only available on PsiLiteralExpression
                if (!(expression instanceof PsiLiteralExpression)) {
                    return;
                }
                for (PsiReference reference : PsiReferenceService.getService().getReferences(expression, new PsiReferenceService.Hints())) {
                    if (reference instanceof ClassWicketIdReference && reference.resolve() == null) {
                        holder.registerProblem(holder.getManager().createProblemDescriptor(expression, "Wicket id reference problem",
                                (LocalQuickFix) null, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true));
                    }
                }
            }
        };
    }
}
