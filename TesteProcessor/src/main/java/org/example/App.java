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
        String targetClassName = "Main";


//        Launcher launcher = new Launcher();
//        launcher.addInputResource(pathHome);
//        launcher.buildModel();
//        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);
//        PDG pdg = new PDG(method);
//        pdg.addDependencesToNodes();

//        for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
//            System.out.println(entry.getKey());
//            for (BasicBlock block : entry.getValue()){
//                System.out.println(block);
//            }
//        }
//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            System.out.println(node.getDataDependenceLocalStatements());
//            System.out.println("---------------------------");
//        }
//        for(BasicBlock block : pdg.getCfg().getBasicBlocks()){
//            System.out.println(block);
//            System.out.println(block.getControlDependent());
//            System.out.println("----------------------");
//            for (GraphEdgeBasicBlock edge : block.getOutgoingEdges()){
//                System.out.println(edge);
//                if(edge.getIsControlEdgeCFG()){
//                    System.out.println("Control edge CFG");
//                }
//                else{
//                    System.out.println("Not control Edge CFG");
//                }
//            }
//        }

        MoveMethodRefefactoring moveMethod = new MoveMethodRefefactoring(pathHome, "printDocument");

        for(Map.Entry<PDGSlice, CtMethod>entry : moveMethod.getCandidateMap().entrySet()){
            entry.getKey().printSlice();
            System.out.println("MapAux");
            System.out.println(entry.getValue());
            //System.out.println(entry.getKey().getMapAux());
        }
   }
}