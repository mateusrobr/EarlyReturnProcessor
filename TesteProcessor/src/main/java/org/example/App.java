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

//        if(args.length == 2){
//            String pathHome = args[0];
//            String targetMethod = args[1];
//            MoveMethodRefefactoring moveMethod = new MoveMethodRefefactoring(pathHome, targetMethod);
//            for(Map.Entry<PDGSlice, CtMethod>entry : moveMethod.getCandidateMap().entrySet()){
//                entry.getKey().printSlice();
//                System.out.println(entry.getValue().prettyprint());
//                System.out.println("If statements that need to continue in the original Method");
//                System.out.println(moveMethod.getPdg().getRemaingNodes().get(entry.getKey().getLocalVariable()));
//                //System.out.println(entry.getKey().getMapAux());
//            }
//        }
//        else{
//            System.out.println(args.length);
//            System.out.println("Incorrect Number of arguments, the correct way to use it is first argument being the path to the class and the seconde one being the name of the method ");
//        }

        String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        //String path = "C:\\Users\\Kurumi\\Desktop\\ERP\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting";
        String pathExample = "C:\\Users\\Kurumi\\Desktop\\SpoonTests\\spoonTests\\src\\main\\java\\org\\example";
        String pathHome = "C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src";
        String pathDell = "/home/dev/Área de Trabalho/códigos-teste/untitled";
        String targetClassName = "Main";
        String targetMethod = "printDocument";
//        Launcher launcher = new Launcher();
//        launcher.addInputResource(pathExample);
//        launcher.buildModel();
//        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument1")).get(0);
//        //System.out.println(method.prettyprint());
//        PDG pdg = new PDG(method);
//
//
//        for(GraphNode node : pdg.getCfg().getAllNodes()){
//            System.out.println(node);
//            System.out.println("DataDependence");
//            for(GraphEdgeNode edge : node.getDataDependenceLocalStatements()){
//                System.out.println(edge);
//            }
//            System.out.println("---------------------------------");
//            System.out.println("AntiDependence");
//            for(GraphEdgeNode edge : node.getAntiDependence()){
//                System.out.println(edge);
//            }
//            System.out.println("-------------------------------------");
//
//            System.out.println("OutputDependence");
//            for(GraphEdgeNode edge : node.getOutPutDependence()){
//                System.out.println(edge);
//            }
//            System.out.println("---------------------------------------------");
//        }
//        for(BasicBlock block : pdg.getCfg().getBasicBlocks()){
//            System.out.println(block);
//            System.out.println(block.getControlDependent());
//            System.out.println(block.getOutgoingEdges());
//            System.out.println("--------------------------------------------------");
        //}

        //String pathHome = args[0];
        //String targetMethod = args[1];
        MoveMethodRefefactoring moveMethod = new MoveMethodRefefactoring(pathHome, targetMethod);
        for(Map.Entry<PDGSlice, CtMethod>entry : moveMethod.getCandidateMap().entrySet()){
            //entry.getKey().printSlice();
            System.out.println(entry.getKey().getLocalVariable());
            System.out.println(entry.getValue().prettyprint());
//            System.out.println("If statements that need to continue in the original Method");
//            System.out.println(moveMethod.getPdg().getRemaingNodes().get(entry.getKey().getLocalVariable()));
            //System.out.println(entry.getKey().getMapAux());
        }
   }
}