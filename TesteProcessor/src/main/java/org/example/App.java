package org.example;

import org.eclipse.jdt.core.dom.Block;
import spoon.FluentLauncher;
import spoon.Launcher;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.NamedElementFilter;
import java.util.ArrayList;
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

        for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
            System.out.println(entry);
        }


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
