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

import java.util.*;


/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

            String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
            //String path = "C:\\Users\\Kurumi\\Desktop\\ERP\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting";
            String pathExample = "C:\\Users\\Kurumi\\Desktop\\SpoonTests\\spoonTests\\src\\main\\java\\org\\example";
            String pathHome = "C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src";
            String targetClassName = "Main";
            String targetMethod = "printDocument";

//
//
//        Launcher launcher = new Launcher();
//        launcher.addInputResource(pathHome);
//        launcher.buildModel();
//        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "testeIfElseIf")).get(0);
//        System.out.println(method.prettyprint());
//        PDG pdg = new PDG(method);
//
//        List<GraphNode> visitedNodes = new ArrayList<>();
//        pdg.transverseThroughGraph(pdg.getCfg().getAllNodes().get(6), pdg.getCfg().getAllNodes().get(8),pdg.getCfg().getAllNodes().get(6) ,visitedNodes);
//        pdg.transverseThroughGraph(pdg.getCfg().getAllNodes().get(7), pdg.getCfg().getAllNodes().get(8),pdg.getCfg().getAllNodes().get(7), visitedNodes);
//        pdg.transverseThroughGraph(pdg.getCfg().getAllNodes().get(2), pdg.getCfg().getAllNodes().get(8),pdg.getCfg().getAllNodes().get(2),visitedNodes);
//        System.out.println(visitedNodes);
//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            for(GraphEdgeNode edge : node.getDataDependenceLocalStatements()){
//                System.out.println(edge);
//            }
//            System.out.println("-----------------------------------");
//        }
//        //System.out.println(pdg.getAllBoundaryBlocksForCompleteComputation());
//        for(BasicBlock block : pdg.getCfg().getBasicBlocks()){
//            System.out.println(block);
//            for(GraphNode node : block.getNodes()){
//                System.out.println(node);
//            }
//            System.out.println("-----------------------------------");
//        }
//        Map<GraphNode, List<BasicBlock>> graphNodeListMap = pdg.getAllBoundaryBlocksForCompleteComputation();
//        Map<GraphNode, List<GraphNode>> statementsThatArePartOfCompleteCOmputation = new LinkedHashMap<>();
//        Map<GraphNode, List<GraphNode>> statementsThatAreNotPartOfCompleteComputation = new LinkedHashMap<>();
//        for(Map.Entry<GraphNode, List<BasicBlock>> entry : graphNodeListMap.entrySet()){
//            List<GraphNode> nodes = new ArrayList<>();
//            for(BasicBlock block : entry.getValue()){
//                nodes.addAll(block.getNodes());
//            }
//            statementsThatArePartOfCompleteCOmputation.put(entry.getKey(), nodes);
//        }
//        for(Map.Entry<GraphNode, List<GraphNode>> entry : statementsThatArePartOfCompleteCOmputation.entrySet()){
//            List<GraphNode> nodes = new ArrayList<>();
//            for(GraphNode node : pdg.getCfg().getAllNodes()){
//                if(!entry.getValue().contains(node)){
//                    nodes.add(node);
//                }
//            }
//            statementsThatAreNotPartOfCompleteComputation.put(entry.getKey(), nodes);
//        }
//
//        for(Map.Entry<GraphNode, List<GraphNode>> entry: statementsThatArePartOfCompleteCOmputation.entrySet()){
//            System.out.println(entry.getKey());
//            for (GraphNode node : statementsThatArePartOfCompleteCOmputation.get(entry.getKey())){
//                for(GraphEdgeNode edge : node.getOutgoingEdges()){
//                    if(edge.getIsControlEdge()){
//                       if(statementsThatAreNotPartOfCompleteComputation.get(entry.getKey()).contains(edge.getDst())){
//                           System.out.println("Node is indispensable: " + node);
//                       }
//                    }
//                }
//            }
//            System.out.println("-------------------------------------------");
//        }
//        for(Map.Entry<GraphNode, List<GraphNode>> entry: statementsThatAreNotPartOfCompleteComputation.entrySet()){
//            System.out.println(entry.getKey());
//            for (GraphNode node : statementsThatAreNotPartOfCompleteComputation.get(entry.getKey())){
//                for(GraphEdgeNode edge : node.getDataDependenceLocalStatements()){
//                    if(statementsThatArePartOfCompleteCOmputation.get(entry.getKey()).contains(edge.getDst())){
//                        System.out.println(edge);
//                        System.out.println("Node indispesable data: " + edge.getDst());
//                    }
//                }
//            }
//            System.out.println("-------------------------------------------");
//        }


//        System.out.println(statementsThatArePartOfCompleteCOmputation);
//        for(Map.Entry<GraphNode, List<GraphNode>> entry : statementsThatArePartOfCompleteCOmputation.entrySet()){
//            System.out.println("Variable: " + entry.getKey());
//            for (GraphNode node : entry.getValue()){
//                System.out.println(node);
//            }
//        }

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
//            //System.out.println(block.getControlDependent());
//            for (GraphEdgeBasicBlock edge : block.getOutgoingEdges()){
//                System.out.println(edge);
//                System.out.println("----------------------------");
//            }
//        }

        MoveMethodRefefactoring moveMethod = new MoveMethodRefefactoring(pathHome, targetMethod);

        for(Map.Entry<PDGSlice, CtMethod>entry : moveMethod.getCandidateMap().entrySet()){
            entry.getKey().printSlice();
            System.out.println("MapAux");
            System.out.println(entry.getValue().prettyprint());
            //System.out.println(entry.getKey().getMapAux());
        }

   }
}