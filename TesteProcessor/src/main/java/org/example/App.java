package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String path = "F:\\Bolsa\\TesteIFS\\src";
        String targetClassName = "Main";
        /*Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        CtModel model = launcher.buildModel();
        EarlyReturnProcessor earlyReturnProcessor = new EarlyReturnProcessor();


        CtClass targetClassSpoon = (CtClass) model.getElements(new NamedElementFilter(CtClass.class, "Main")).get(0);


        System.out.println(targetClassSpoon.prettyprint());
        for (CtMethod method : targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class))){

            if(earlyReturnProcessor.isToBeProcessed(method)){
                earlyReturnProcessor.process(method);
            }

        }
        System.out.println(targetClassSpoon.prettyprint());*/

        BlockHierarchy blockHierarchy= new BlockHierarchy(path, targetClassName);
        VariableDependencyCreator variableDependencyCreator = new VariableDependencyCreator(path, targetClassName);
        variableDependencyCreator.testingClassSpoon();


        List<CtCodeElement> codeElementList = variableDependencyCreator.getElementsInCorrectPosition();
        //System.out.println(codeElementList.get(2).getElements(new TypeFilter<>(CtAssignment.class)));
        //System.out.println(codeElementList);
        /*System.out.println(codeElementList.get(2));
        System.out.println(codeElementList.get(2).getClass());
        System.out.println(codeElementList.get(2).getDirectChildren());
        for(Object element : codeElementList.get(2).getDirectChildren()){
            System.out.println(element);
            System.out.println(element.getClass());
        }*/


        /*variableDependencyCreator.addingAllVariablesToVariableField();
        variableDependencyCreator.printVariableField();*/




    }
}
