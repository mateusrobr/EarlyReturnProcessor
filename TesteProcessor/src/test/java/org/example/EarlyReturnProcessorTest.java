package org.example;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import static org.junit.Assert.*;

public class EarlyReturnProcessorTest {

    @Test
    public void EarlyReturnProcess() {
        EarlyReturnProcessor earlyReturnProcessor = new EarlyReturnProcessor();

        CtClass targetClassSpoon = getTargetClass();

        earlyReturnProcessor.process((CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(0));

        CtMethod expectedMethod = (CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(1);
        CtMethod actualMethod = (CtMethod) targetClassSpoon.getElements(new TypeFilter(CtMethod.class)).get(0);


        assertEquals(expectedMethod.getBody().prettyprint(), actualMethod.getBody().prettyprint());

    }

    public CtClass getTargetClass(){
        Launcher launcher = new Launcher();
        launcher.addInputResource("F:\\Bolsa\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting");
        launcher.buildModel();
        CtModel model = launcher.getModel();

        CtClass targetClassSpoon = model.getElements(new TypeFilter<>(CtClass.class)).get(0);
        System.out.println(targetClassSpoon);

        return targetClassSpoon;
    }

}