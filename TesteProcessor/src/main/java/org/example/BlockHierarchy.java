package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockHierarchy {
    private String path;
    private String targetClassName;
    private CtClass targetClassSpoon;

    public BlockHierarchy(String path, String targetClassName){
        this.path = path;
        this.targetClassName = targetClassName;
        this.targetClassSpoon = getTargetClassSpoon();
    }

    public CtClass getTargetClassSpoon(){
        Launcher launcher = new Launcher();
        CtModel model;
        launcher.addInputResource(path);
        launcher.buildModel();
        model = launcher.getModel();

        return (CtClass) model.getElements(new NamedElementFilter(CtClass.class, targetClassName)).get(0);

   }

   public void printCtStatements(){
        CtMethod ctMethod = (CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(1);
       CtBlock ctBlock = (CtBlock) ctMethod.getElements(new TypeFilter(CtBlock.class)).get(0);
       //System.out.println(ctBlock.getElements(new TypeFilter(CtCodeElement.class)));
       List CtCodeElementList = new ArrayList();
       for(Object element :  ctBlock.getElements(new TypeFilter(CtCodeElement.class))){
           CtCodeElement elementToListo = (CtCodeElement) element;
           if(elementToListo.getParent() == ctBlock){
               CtCodeElementList.add(elementToListo);
           }
       }

       System.out.println(CtCodeElementList);
   }

   public void blockHierarchyMaker(){

   }

}
