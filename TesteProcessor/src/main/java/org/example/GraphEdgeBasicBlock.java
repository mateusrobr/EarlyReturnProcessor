package org.example;

public class GraphEdgeBasicBlock {

    private BasicBlock src;
    private BasicBlock dst;

    private boolean controlEdge;

    private boolean controlEdgeCDG;

    private boolean isElseStamentInCode;
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

    public void setIsControlEdge(boolean isControlEdge){
        this.controlEdge = isControlEdge;
    }
    public boolean isControlEdge(){
        return controlEdge;
    }

    public void setIsControlEdgeCDG(boolean isControlEdgeCDG){
        this.controlEdgeCDG = isControlEdgeCDG;
    }
    public boolean getIsControlEdgeCDG(){
        return controlEdgeCDG;
    }

    public void setElseStamentInCode(boolean isElseStamentInCode){
        this.isElseStamentInCode = isElseStamentInCode;
    }

    public boolean isElseStamentInCode() {
        return isElseStamentInCode;
    }

    public String toString(){
        return "src: " + src + " dst: " + dst;
    }
}
