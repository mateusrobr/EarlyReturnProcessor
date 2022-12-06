package org.example;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtBlockImpl;
import spoon.support.reflect.code.CtIfImpl;
import spoon.support.reflect.code.CtSwitchImpl;
import spoon.support.reflect.code.CtWhileImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        //String targetClassName = "TesteIfs";


        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.buildModel();

        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "teste01")).get(0);



        CFG cfg = new CFG(method);
        cfg.printNodesFromBasicBlocks();


    }
}
