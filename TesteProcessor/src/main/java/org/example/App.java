package org.example;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        String targetClassName = "TesteIfs";
        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        CtModel model = launcher.buildModel();
        EarlyReturnProcessor earlyReturnProcessor = new EarlyReturnProcessor(path, targetClassName);
        CtClass targetClassSpoon = null;


        for (int i = 0 ; i < model.getElements(new TypeFilter<>(CtClass.class)).size() ; i++){
            if (model.getElements(new TypeFilter<>(CtClass.class)).get(i).getSimpleName().equals(targetClassName)){
                targetClassSpoon = model.getElements(new TypeFilter<>(CtClass.class)).get(i);
            }
        }
        System.out.println(targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class)).get(0).prettyprint());
        for (CtMethod method : targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class))){

            earlyReturnProcessor.process(method);
        }
        System.out.println(targetClassSpoon.getElements(new TypeFilter<>(CtMethod.class)).get(0).prettyprint());

    }
}
