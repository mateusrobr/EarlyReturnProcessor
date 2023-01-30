package org.example;

import spoon.reflect.code.CtStatement;

public class GraphEdge {

    private GraphNode src;
    private GraphNode dst;


    public GraphEdge(){
    }
    public GraphNode getDst(){
        return dst;
    }

    public void setDst(GraphNode src){
        this.dst = src;
    }

    public void setSrc(GraphNode src){
        this.src = src;
    }

    public GraphNode getSrc() {
        return src;
    }

    public String toString(){
        return "src: " + src + "\n" +" dst: " + dst;
    }
}
