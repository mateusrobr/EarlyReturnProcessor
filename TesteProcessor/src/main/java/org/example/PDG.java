package org.example;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

public class PDG {
    private CFG cfg;
    private List<CtLocalVariable> allLocalVariablesForThisMethod;
    public PDG(CtMethod targetMethod){
        cfg = new CFG(targetMethod);
        allLocalVariablesForThisMethod = new ArrayList<>();
        allLocalVariablesForThisMethod = targetMethod.getElements(new TypeFilter<>(CtLocalVariable.class));
    }

    public List<CtLocalVariable> getAllLocalVariablesForThisMethod(){
        return allLocalVariablesForThisMethod;
    }
}
