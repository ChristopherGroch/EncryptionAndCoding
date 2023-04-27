package pl.model.HuffmanCoding;

public class Node implements Comparable<Node>{
    private Node leftNode;
    private Node rightNode;
    private int amount;
    private char character;
    private final boolean isLeaf;

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public int getAmount() {
        return amount;
    }

    public char getCharacter() {
        return character;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public Node(Node leftNode, Node rightNode) {

        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.isLeaf = false;
        this.amount = findAmount();

    }
    public Node(char character, int amount) {

        this.character = character;
        this.amount = amount;
        this.isLeaf = true;

    }
    public Node(){
        this.isLeaf = false;
    }
    public int findAmount() {
        return leftNode.getAmount() + rightNode.getAmount();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public int compareTo(Node o) {
        return getAmount() - o.getAmount();
    }
}
