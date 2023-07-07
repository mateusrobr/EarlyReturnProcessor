package org.example;

import spoon.Launcher;
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
        System.out.println(method.getParameters().get(0));
        for(Object object : method.getBody().filterChildren(new TypeFilter<>(CtParameterReference.class)).list()){
            CtReference parameter = (CtReference) object;
            System.out.println(pdg.getGraphNodeFromCtReference(parameter, pdg.getCfg().getAllCtStatements()));
        }

//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            System.out.println(node.getStatement().getClass());
//        }


//        for (Map.Entry<GraphNode, List<GraphNode>> entry : pdg.getLocalVariableAssigmentOcurrences().entrySet()) {
//            System.out.println("Variable: " + entry.getKey());
//            System.out.println("Statements:");
//            for (GraphNode node : entry.getValue()) {
//                System.out.println(node);
//            }
//            System.out.println("-------------------------------------");
//
//        }
        //System.out.println(pdg.getLocalVariablesOcurrences());
        //pdg.addDependencesToNodes();
//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            for (GraphEdgeNode edge : node.getOutgoingEdges()){
//                System.out.println(edge);
//                System.out.println(edge.getIsControlEdge());
//            }
//        }

//        for(BasicBlock block : pdg.getCfg().getBasicBlocks()){
//            System.out.println(block);
//            for (GraphEdgeBasicBlock edge : block.getOutgoingEdges()){
//                System.out.println(edge);
//                System.out.println(edge.isControlEdge());
//            }
//        }
//        pdg.getAllBoundaryBlocksForCompleteComputation();
//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            System.out.println(node.getDependence());
//        }

    }
}