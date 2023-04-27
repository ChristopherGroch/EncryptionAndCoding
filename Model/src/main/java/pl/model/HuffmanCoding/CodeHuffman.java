package pl.model.HuffmanCoding;

import pl.model.Operations.BitwiseOperations;
import pl.model.Operations.FileOperations;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

public class CodeHuffman {

    private HashMap<Character,Integer> amountMap;
    private HashMap<Character,String> dict;
    private Node root;
    private char[] originalText = null;
    private String codedTex = null;
    public CodeHuffman(){}

    public void innitHuffmanWithString(String string){

        originalText = string.toCharArray();
        dict = new HashMap<>();
        amountMap = new HashMap<>();
        createAmountMap();
        createTree();
        createDict("",root);
        codedTex = null;
    }
    public void innitHuffmanWithFile(String file){

        originalText = Objects.requireNonNull(FileOperations.loadStringFromFile(file)).toCharArray();
        dict = new HashMap<>();
        amountMap = new HashMap<>();
        createAmountMap();
        createTree();
        createDict("",root);
        codedTex = null;
    }

    private void createAmountMap(){

        for (char c:originalText) {
            if(amountMap.containsKey(c)){
                int amount = amountMap.get(c) + 1;
                amountMap.replace(c,amount);
            } else {
                amountMap.put(c,1);
            }
        }
    }

    private void createTree(){

        PriorityQueue<Node> queue = new PriorityQueue<>();

        for(char c : amountMap.keySet()){
            queue.add(new Node(c,amountMap.get(c)));
        }

        while (queue.size() > 1){
            queue.add( new Node(queue.poll(),queue.poll()));

        }
        root = queue.poll();
    }

    private void createDict(String string, Node node){
        if(node.isIsLeaf()){
            dict.put(node.getCharacter(),string);
            return;
        }
        createDict(string.concat("0"),node.getLeftNode());
        createDict(string.concat("1"),node.getRightNode());

    }

    private String code(){

        StringBuilder out = new StringBuilder();
        for(char c:originalText){
            out.append(dict.get(c));
        }
        return out.toString();
    }


    private String saveTree(String string,Node node) {
        if(node.isIsLeaf()){
            string += '1';
            byte b = (byte) node.getCharacter();
            StringBuilder stringBuilder = new StringBuilder(string);
            for(int i = 0; i < 8; i++) {
                stringBuilder.append((char) (BitwiseOperations.getBit(b, i) + '0'));
            }
            string = stringBuilder.toString();
            return string;
        }

        string += '0';
        string = saveTree(string,node.getLeftNode());
        string = saveTree(string,node.getRightNode());
        return string;
    }

    public String codeToString(){

        if(codedTex == null) {
            String out = saveTree("", root);
            codedTex = out + code();
        }
        return codedTex;
    }

    public void codeToFile(String file){

        if(codedTex == null) {
            codeToString();
        }
        FileOperations.saveStringAsBitsToFile(file,codedTex);
    }

}
