package org.example;

import spoon.Launcher;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class EarlyReturnProcessor extends AbstractProcessor<CtMethod>{

    @Override
    public void process(CtMethod ctMethod) {
        Launcher launcher = new Launcher();
        CtStatement returnStatement = launcher.getFactory().createCodeSnippetStatement("return");

        CtBlock elseStatement;

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getThenStatement().insertAfter(returnStatement);

        elseStatement = ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement();
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement().delete();

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).insertAfter( (CtStatement) elseStatement);
    }
}
