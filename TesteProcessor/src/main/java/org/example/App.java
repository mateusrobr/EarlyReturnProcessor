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
        launcher.addInputResource(pathHome);
//        FluentLauncher fluentLauncher = new FluentLauncher();
//        fluentLauncher.outputDirectory("C:\\Users\\Mateus\\Desktop\\metodosteste\\testemetodos\\src");

        launcher.buildModel();
        CtMethod method = (CtMethod) launcher.getModel().getElements(new NamedElementFilter(CtMethod.class, "printDocument")).get(0);

        PDG pdg = new PDG(method);

//        for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
//            PDGSlice candidate = new PDGSlice(entry.getKey(), entry.getValue());
//            System.out.println(entry);
//        }
//        System.out.println(pdg.getLocalVariableAssigmentOcurrences());
        ;
        for(List<List<BasicBlock>> blockList : pdg.getBoundaryBlocksForLocalAssigmentsInSeparetedLists().values()){
            System.out.println(blockList);
            System.out.println();
        }
        System.out.println(pdg.getIntersectionOfBoundaryBlocks());

        // this slice of code below need to be put in a spoon processor
//        CtMethod createdMethod = launcher.createFactory().createMethod();
//        createdMethod.setSimpleName("metodoMovido");
//        createdMethod.setVisibility(ModifierKind.PUBLIC);
//        CtTypeReference<String> typeReference = launcher.createFactory().createCtTypeReference(String.class);
//        createdMethod.setType(typeReference);
//        CtCodeSnippetStatement snippetStatement = launcher.getFactory().createCodeSnippetStatement();
//        CtBlock block = launcher.createFactory().Code().createCtBlock(snippetStatement);
//        List<CtStatement> statementThatAreBeingMoved = new ArrayList<>();
//        for(List<BasicBlock> basicBlocks : pdg.getAllBoundaryBlocksForCompleteComputation().values()){
//            BasicBlock bloco_1 = basicBlocks.get(0);
//
//            for(GraphNode node : bloco_1.getNodes()){
//                CtStatement statement = node.getStatement();
//                statementThatAreBeingMoved.add(statement.clone());
//            }
//            break;
//        }
//        block.setStatements(statementThatAreBeingMoved);
//        createdMethod.setBody(block);
//        System.out.println(method.prettyprint());
//        System.out.println(createdMethod.prettyprint());
        }
    }
