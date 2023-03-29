package org.example;

import spoon.Launcher;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
        String path = "C:\\Users\\Kurumi\\Desktop\\ERP\\EarlyReturnProcessor\\TesteProcessor\\src\\main\\java\\BaseMethodsForTesting";
        String pathExample = "C:\\Users\\Kurumi\\Desktop\\SpoonTests\\spoonTests\\src\\main\\java\\org\\example";
        String pathHome = "C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src";
        //String targetClassName = "Main";
        //String path = "/home/facomp/IdeaProjects/IfCounterRepo/IfCounter/src/main/java/org/example";
       // String targetClassName = "TesteIfs";


        Launcher launcher = new Launcher();
        launcher.addInputResource(pathHome);
        launcher.buildModel();

        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);



        PDG pdg = new PDG(method);
        System.out.println(pdg.getAllLocalVariablesForThisMethod());
        //CFG cfg = new CFG(method);
        //cfg.printNodesFromBasicBlocks();

        System.out.println("Edges: ");

        /*for(GraphNode node : cfg.getAllNodes()){

            System.out.println(node);
            System.out.println("Edge: ");
            System.out.println(node.getOutgoingEdges());
            System.out.println(" ----------------");
        }*/



        //cfg.printNodesFromBasicBlocks();

        //for(BasicBlock basicBlock : cfg.getBasicBlocks()){
            //System.out.println(basicBlock);
            //System.out.println(basicBlock.getNodes());
            //System.out.println("id do block: " +basicBlock.getId());
            //System.out.println(basicBlock.getOutgoingEdges());
            //System.out.println(basicBlock.getReachableBlocks());
           // System.out.println("Dominated:");
           // System.out.println(basicBlock.getDominatedBlocks());
           // System.out.println("Reachad");
           // System.out.println(basicBlock.getReachableBlocks());
       // }

    }
}
