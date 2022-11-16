package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtLocalVariableImpl;

import java.util.ArrayList;
import java.util.List;

public class VariableDependencyCreator {
    private String path;
    private String targetClassName;
    private CtClass targetClassSpoon;

    private List<Variable> variables;
    public VariableDependencyCreator(String path, String targetClassName){
        this.path = path;
        this.targetClassName = targetClassName;
        this.targetClassSpoon = getTargetClassSpoon();
        variables = new ArrayList();
    }

    public CtClass getTargetClassSpoon(){
        Launcher launcher = new Launcher();
        CtModel model;
        launcher.addInputResource(path);
        launcher.buildModel();
        model = launcher.getModel();

        return (CtClass) model.getElements(new NamedElementFilter(CtClass.class, targetClassName)).get(0);
    }

    public List getElementsInCorrectPosition(){
        CtMethod ctMethod = (CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(1);
        CtBlock ctBlock = (CtBlock) ctMethod.getElements(new TypeFilter(CtBlock.class)).get(0);


        List<CtCodeElement> CtCodeElementList = new ArrayList();
        for(Object element :  ctBlock.getElements(new TypeFilter(CtCodeElement.class))){
            CtCodeElement candidateElementToEnterList = (CtCodeElement) element;
            if(candidateElementToEnterList.getParent() == ctBlock){
                CtCodeElementList.add(candidateElementToEnterList);
            }
        }

        return CtCodeElementList;
    }

    public void printStatementCorrectPosition(){
        for(Object codeElement : getElementsInCorrectPosition()){
            System.out.println(codeElement);
            System.out.println(codeElement.getClass());
        }
    }

    public void addingAllVariablesToVariableField(){
        for(Object element :  getElementsInCorrectPosition()){
            if(element.getClass().toString().equals(CtLocalVariableImpl.class.toString())){
                variables.add(new Variable((CtVariable) element));
            }
        }
    }

    public void printVariableField(){
        for (Variable variable : variables){
            System.out.println(variable.getCtVariable());
            System.out.println(variable.getAllDependencies());
        }
    }

    public void buildingVariableDependencies(){
    }

    public void testingClassSpoon(){
        CtMethod targetMethod = (CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(1);
        System.out.println(targetMethod.getElements(new TypeFilter<>(CtTypedElement.class)));
        System.out.println();
        System.out.println(targetMethod.getElements(new TypeFilter<>(CtTypedElement.class)));
    }

}
