package org.example;

import spoon.FluentLauncher;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveMethodRefefactoring {

    private PDG pdg;

    private Launcher launcher;
    private CtModel model;
    private List<PDGSlice> candidates;

    public MoveMethodRefefactoring(String path,String targetMethod){
        this.candidates = new ArrayList<>();
        this.launcher = new Launcher();
        this.launcher.addInputResource(path);
        this.launcher.setSourceOutputDirectory(path);
        this.model = this.launcher.buildModel();
        CtMethod method = (CtMethod) this.model.getElements(new NamedElementFilter(CtMethod.class, targetMethod)).get(0);

        this.pdg = new PDG(method);
        this.pdg.addDependencesToNodes();
        for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
            //System.out.println(entry.getValue());
            candidates.add(new PDGSlice(entry.getKey(),entry.getValue(),launcher));
        }
        for(PDGSlice candidate: candidates){
            candidate.printSlice();
            //System.out.println(candidate.produceNewMethod());
            candidate.produceNewMethod();
        }
    }
}
