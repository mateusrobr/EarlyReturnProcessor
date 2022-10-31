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
        Launcher launcher = new Launcher();
        launcher.addInputResource("/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example");
        CtModel model = launcher.buildModel();
        EarlyReturnProcessor earlyReturnProcessor = new EarlyReturnProcessor();


        //System.out.println(model);
        CtMethod testeMetodo = model.getElements(new TypeFilter<>(CtClass.class)).get(3).getElements(new TypeFilter<>(CtMethod.class)).get(0);
        System.out.println(testeMetodo.prettyprint());

        earlyReturnProcessor.process(testeMetodo);
        System.out.println(testeMetodo.prettyprint());
    }
}
