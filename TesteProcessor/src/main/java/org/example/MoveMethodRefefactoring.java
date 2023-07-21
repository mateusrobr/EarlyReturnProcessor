package org.example;

import spoon.FluentLauncher;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.NamedElementFilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MoveMethodRefefactoring {

    private PDG pdg;

    private Launcher launcher;
    private CtModel model;
    private List<PDGSlice> candidates;
    private Map<PDGSlice, CtMethod> candidateMap;

    public MoveMethodRefefactoring(String path,String targetMethod){
        this.candidates = new ArrayList<>();
        this.launcher = new Launcher();
        this.launcher.addInputResource(path);
        this.launcher.setSourceOutputDirectory(path);
        this.model = this.launcher.buildModel();
        this.candidateMap = new LinkedHashMap<>();
        CtMethod method = (CtMethod) this.model.getElements(new NamedElementFilter(CtMethod.class, targetMethod)).get(0);

        this.pdg = new PDG(method);
        this.pdg.addDependencesToNodes();
        for(Map.Entry<GraphNode, List<BasicBlock>> entry : pdg.getAllBoundaryBlocksForCompleteComputation().entrySet()){
            candidates.add(new PDGSlice(entry.getKey(),entry.getValue(),launcher));
        }
        for(PDGSlice candidate: candidates){

            candidateMap.put(candidate, candidate.produceNewMethod());
        }
    }
    public PDG getPdg(){
        return  this.pdg;
    }
    public Map<PDGSlice, CtMethod> getCandidateMap(){
        return this.candidateMap;
    }
}
