package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class EarlyReturnProcessor extends AbstractProcessor<CtMethod>{
    private String path;
    private String targetClassName;

    private final int FIRST_IF = 0;

    public EarlyReturnProcessor(String path, String targetClassName){
        this.path = path;
        this.targetClassName = targetClassName;
    }

    public void earlyReturnRefactor() {
        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        CtModel model = launcher.buildModel();
        CtClass targetClassSpoon = null;
        for (int i = 0; i < model.getElements(new TypeFilter<>(CtClass.class)).size(); i++) {
            if (model.getElements(new TypeFilter<>(CtClass.class)).get(i).getSimpleName().equals(targetClassName)) {
                targetClassSpoon = model.getElements(new TypeFilter<>(CtClass.class)).get(i);
            }
        }
        System.out.println("A");

        if(targetClassSpoon.getSimpleName() == null){
            return;
        }
        System.out.println("B");

        for(int i = 0 ; i < targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class)).size() ; i++){
            System.out.println(targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class)).get(i));
            process(targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class)).get(i));
        }
        System.out.println("C");


    }


    @Override
    public void process(CtMethod ctMethod) {
        if(!ctMethod.getType().prettyprint().equals("void")){
            return;
        }

        if(ctMethod.getElements(new TypeFilter<>(CtIf.class)).size() == 0 || ctMethod.getElements(new TypeFilter<>(CtIf.class)).size() > 1){
            return;
        }

        if(ctMethod.getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement() == null){
            return;
        }

        if(ctMethod.getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement().getElements(new TypeFilter<>(CtIf.class)).size() > 0 ){
            return;
        }

        Launcher launcher = new Launcher();
        CtStatement returnStatement = launcher.getFactory().createCodeSnippetStatement("return");

        CtBlock elseStatement;

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getThenStatement().insertAfter(returnStatement);

        elseStatement = ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement();
        //CtCodeSnippetStatement elseSnippet = launcher.getFactory().createCodeSnippetStatement(elseStatement.prettyprint().replace("{","").replace("}","//"));

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement().delete();
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).insertAfter((CtStatementList) elseStatement);
    }
}
