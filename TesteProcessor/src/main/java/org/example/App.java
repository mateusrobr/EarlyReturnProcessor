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

        Launcher launcher = new Launcher();
        launcher.addInputResource(pathHome);
//        FluentLauncher fluentLauncher = new FluentLauncher();
//        fluentLauncher.outputDirectory("C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src");

        launcher.buildModel();
        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);
        PDG pdg = new PDG(method);

//        for(BasicBlock blocks : pdg.getCfg().getBasicBlocks()){
//            for(GraphEdgeBasicBlock edge : blocks.getOutgoingEdges()){
//                if(edge.isControlEdge()){
//                    System.out.println(blocks.getId());
//                    System.out.println(edge);
//                }
//            }
//        }
        for(GraphNode node : pdg.getCfg().getAllNodes()){
            System.out.println(node);
            for(GraphEdgeNode edge : node.getOutgoingEdges()){
                System.out.println("Edge: " + edge);
                if(edge.getIsControlEdge()){
                    System.out.println("é control edge");
                }
                else {
                    System.out.println("Não é control edge");
                }
            }
        }


        //MoveMethodRefefactoring moveMethod = new MoveMethodRefefactoring(pathHome, "printDocument");


   }
}