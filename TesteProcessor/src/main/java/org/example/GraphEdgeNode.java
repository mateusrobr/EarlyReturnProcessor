package org.example;

public class GraphEdgeNode {

    private GraphNode src;
    private GraphNode dst;

    private boolean controlEdge;

    public GraphEdgeNode(){
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

    public void setIsControlEdge(boolean isControlEdge){
        this.controlEdge = isControlEdge;
    }
    public boolean getIsControlEdge(){
        return controlEdge;
    }
    public String toString(){
        return "src: " + src + "\n" +" dst: " + dst;
    }
}
