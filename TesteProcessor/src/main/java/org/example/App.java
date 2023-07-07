package org.example;

import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtParameterReference;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        String path = "C:\\Users\\Kurumi\\Desktop\\ERP\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting";
        String pathExample = "C:\\Users\\Kurumi\\Desktop\\SpoonTests\\spoonTests\\src\\main\\java\\org\\example";
        String pathHome = "C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src";
        //String targetClassName = "Main";


        Launcher launcher = new Launcher();
        launcher.addInputResource(pathHome);
//        FluentLauncher fluentLauncher = new FluentLauncher();
//        fluentLauncher.outputDirectory("C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src");

        launcher.buildModel();
        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);

        PDG pdg = new PDG(method);
//        for(Object object : method.filterChildren(new TypeFilter<>(CtParameter.class)).list()){
//            CtParameter parameter = (CtParameter) object;;
//            CtStatement statementTest = (CtStatement) parameter;
//            //GraphNode node = pdg.getGraphNodeFromCtReference(parameter, pdg.getCfg().getAllCtStatements());
//        }


        //System.out.println(pdg.getLocalVariablesOcurrences());
        pdg.addDependencesToNodes();
//        pdg.getAllBoundaryBlocksForCompleteComputation();
        for(GraphNode node : pdg.getCfg().getAllNodes()){
            System.out.println(node);
            System.out.println("Data dependence for Local Statements: "+node.getDataDependenceLocalStatements());
            System.out.println("Data dapendencie for Parameters: " + node.getDataDependenceParameters());
        }

    }
}