package org.example;

public class GraphEdgeBasicBlock {

    private BasicBlock src;
    private BasicBlock dst;

    public GraphEdgeBasicBlock(){}

    public void setSrc(BasicBlock src){
        this.src = src;
    }

    public void setDst(BasicBlock dst){
        this.dst = dst;
    }

    public BasicBlock getDst() {
        return dst;
    }

    public BasicBlock getSrc() {
        return src;
    }
}
