package pl.model.HuffmanCoding;

import pl.model.Operations.BitwiseOperations;
import pl.model.Operations.FileOperations;

import java.util.Objects;

public class DecodeHuffman {
    private Node root;
    private char[] codedText = null;
    private String originalText = null;

    public DecodeHuffman() {}
    public void innitDecodeHuffmanString(String string){

        codedText = string.toCharArray();
        originalText = null;
    }
    public void innitDecodeHuffmanFile(String file){
        codedText = Objects.requireNonNull(FileOperations.loadBitsAsStringFromFile(file)).toCharArray();
        originalText = null;
    }
    private String decode(String string) {
        char[] chars = string.toCharArray();
        StringBuilder out = new StringBuilder();

        Node iterate = root;
        for(char c :chars){
            if (c == '0'){
                iterate = iterate.getLeftNode();
            }else {
                iterate = iterate.getRightNode();
            }
            if(iterate.isIsLeaf()){
                out.append(iterate.getCharacter());
                iterate = root;
            }
        }
        return out.toString();
    }
    private Node readTree(Integer i){
        if(codedText[i] == '1') {
            i++;
            byte b = 0;
            for(int x = 0;x<8;x++){
                b = BitwiseOperations.setBit(b,x,Integer.parseInt(String.valueOf(codedText[x + i])));
            }
            i += 8;
            char c = (char) b;

            return new Node(c,i);
        }
        i++;
        Node node = new Node();
        node.setLeftNode(readTree(i));
        node.setRightNode(readTree(node.getLeftNode().getAmount()));
        node.setAmount(node.getRightNode().getAmount());
        return node;
    }
    public String decodeToString() {

        if (originalText == null){
            root = readTree(0);
            StringBuilder s = new StringBuilder();

            for(int i = root.getAmount();i<codedText.length;i++){
                s.append(codedText[i]);
            }

            originalText = decode(s.toString());
        }

        return originalText;
    }
    public void decodeToFile(String file){
        if(originalText == null){
            decodeToString();
        }

        FileOperations.saveStringAsString(file,originalText);
    }
}
