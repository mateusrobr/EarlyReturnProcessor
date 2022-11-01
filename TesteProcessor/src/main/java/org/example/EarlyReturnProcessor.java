package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class EarlyReturnProcessor extends AbstractProcessor<CtMethod>{
    private String path;
    private String targetClassName;

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

        if(ctMethod.getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement() == null){
            return;
        }

        if(ctMethod.getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement().getElements(new TypeFilter<>(CtIf.class)).size() > 0 ){
            return;
        }

        Launcher launcher = new Launcher();
        CtStatement returnStatement = launcher.getFactory().createCodeSnippetStatement("return");

        CtBlock elseStatement;

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getThenStatement().insertAfter(returnStatement);

        elseStatement = ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement();
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).getElseStatement().delete();

        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(0).insertAfter( (CtStatement) elseStatement);
    }
}
