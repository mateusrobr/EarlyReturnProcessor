package org.example;

import spoon.reflect.code.CtCodeElement;
import spoon.reflect.declaration.CtVariable;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private CtVariable ctVariable;
    private List<CtCodeElement> allDependencies;

    public Variable(CtVariable ctVariable){
        this.ctVariable = ctVariable;
        allDependencies = new ArrayList<>();
    }

    public CtVariable getCtVariable(){
        return this.ctVariable;
    }
    public List<CtCodeElement> getAllDependencies(){
        return allDependencies;
    }
}
