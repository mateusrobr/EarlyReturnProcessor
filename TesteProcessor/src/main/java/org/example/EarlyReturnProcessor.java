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

    @Override
    public boolean isToBeProcessed(CtMethod candidate) {

        if (candidate.getType().prettyprint().equals("void")) {
            //Verifica se o metodo é void

            if (candidate.getElements(new TypeFilter<>(CtIf.class)).size() == 1 /*|| candidate.getElements(new TypeFilter<>(CtIf.class)).size() > 1*/) {
                //Verifica se tem exatamente um If

                if (candidate.getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement() != null) {
                    //Verifica se o else é existente

                    if (candidate.getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement().getElements(new TypeFilter<>(CtIf.class)).size() == 0) {
                        //Verifica se tem algum If dentro do bloco do Else --implementar a verificação no bloco Then

                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void process(CtMethod ctMethod) {
        CtBlock elseStatement; //Pode ser facilmente retirado porem os argumentos ficariam extremamente grandes

        //ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).insertAfter((CtStatementList) ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement());

        CtStatement returnStatement = getReturnStatement(); // Tambem pode ser facilmente retirado porem o argumento ficaria extramente grande e precisaria do launcher nesse método



        //Insere o "return;" no final do block then
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getThenStatement().insertAfter(returnStatement);

        //Pega o bloco Else
        elseStatement = ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement();

        //Deleta o bloco Else
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).getElseStatement().delete();

        //Coloca o bloco Else após o If
        ctMethod.getBody().getElements(new TypeFilter<>(CtIf.class)).get(FIRST_IF).insertAfter((CtStatementList) elseStatement);
    }

    public CtStatement getReturnStatement(){
        Launcher launcher = new Launcher();
        return launcher.getFactory().createCodeSnippetStatement("return");
    }

}
