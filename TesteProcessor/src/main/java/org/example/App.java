package org.example;

import spoon.Launcher;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;

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
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        // String targetClassName = "TesteIfs";


        Launcher launcher = new Launcher();
        launcher.addInputResource(pathExample);
        launcher.buildModel();

        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);


        //System.out.println(method.prettyprint());
        PDG pdg = new PDG(method);
        //System.out.println(pdg.getStatementsLocalVariableIsAssigned());
        //CFG cfg = new CFG(method);
        //System.out.println(cfg.getBasicBlocks());

        /*for(Map.Entry<GraphNode,List<GraphNode>> entry : pdg.getStatementsLocalVariableIsAssigned().entrySet()){
            for(GraphNode localVariableAssignedOcurrence : entry.getValue()){
                System.out.println("Boundary block for: " + localVariableAssignedOcurrence);
                System.out.println(pdg.getBoundaryBlocksForLocalVariableOcurrence(entry.getKey(), localVariableAssignedOcurrence));

                }
            }
        }*/
        for (Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksWithRepeatedBlocks().entrySet()) {
            PDGSlice slice = new PDGSlice(entry.getKey(), entry.getValue());
            slice.printSlice();
            System.out.println(slice.getBoundaryBlockIntersectionFromBoundaryBlockRaw());
        }


        //System.out.println(pdg.getAllBoundaryBlocksForCompleteComputation());
        /*for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
            System.out.println("Variable: " + entry.getKey());
            System.out.println("Statements that are part of the complete computation");
            for(BasicBlock basicBlock : entry.getValue()){
                for(GraphNode node : basicBlock.getNodes()){
                    System.out.println(node);
                    //System.out.println("CtReferences: ");
                    if(node.getStatement() instanceof CtIfImpl){
                        //System.out.println(((CtIfImpl) node.getStatement()).getCondition().getElements(new TypeFilter<>(CtVariableReference.class)));
                    }
                    else {
                        //System.out.println(node.getStatement().getElements(new TypeFilter<>(CtVariableReference.class)));
                    }
                }
            }
            System.out.println("----------------------------------");
        }*/
    }
}
