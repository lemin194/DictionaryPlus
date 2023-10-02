package dictionary.models.Entity;

import java.util.ArrayList;
import java.util.Arrays;

/*
 ** Template code for trie.
 * reference: https://vnoi.info/wiki/algo/data-structures/trie.md
 *
 */
public class Trie {
    /*
     ** Using alphabet, not extended ASCII.
     */
    private static final int maximumChar = 26;

    private class Node {
        Node[] children;
        boolean isEnd;

        public Node() {
            children = new Node[maximumChar];
            isEnd = false;
        }
    }
    private Node root;

    /*
     ** Initialize Trie with single node is root.
     * Root does not contain any character.
     */
    public Trie() {
        root = new Node();
        root.isEnd = true;
    }

    /*
     ** Adding word into dictionary.
     * Note that we need to mark ending for the last character of adding word.
     */
    public void add(String word) {
        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            int j = word.charAt(i) - 'a';
            if (currentNode.children[j] == null) {
                currentNode.children[j] = new Node();
            }
            currentNode = currentNode.children[j];
        }
        currentNode.isEnd = true;
    }

    /*
     ** Delete word from dictionary.
     */
    public void delete(String word) {
        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            int j = word.charAt(i) - 'a';
            if (currentNode.children[j] == null) {
                return;
            } else {
                currentNode = currentNode.children[j];
            }
        }
        currentNode.isEnd = false;
    }

    /*
     ** Searching for whole word.
     * There's the case last character of searching word pass till end but not the ending
     * character.
     * Example: fish but when we search for fis.
     */
    public boolean search(String word) {
        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            int j = word.charAt(i) - 'a';
            if (currentNode.children[j] == null) {
                return false;
            } else {
                currentNode = currentNode.children[j];
            }
        }
        return currentNode.isEnd;
    }

    /*
     ** Only searching for prefix, not strict as search() method.
     */
    public boolean prefixSearch(String word) {
        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            int j = word.charAt(i) - 'a';
            if (currentNode.children[j] == null) {
                return false;
            } else {
                currentNode = currentNode.children[j];
            }
        }
        return true;
    }

    /*
     ** Get words contain prefix string.
     */
    public void getWords(String prefix, ArrayList<String> words, Node currentNode) {
        if (currentNode == null) {
            return;
        }
        if (currentNode.isEnd && currentNode != root) {
            words.add(prefix);
        }
        for (int i = 0; i < maximumChar; i++) {
            Node nextNode = currentNode.children[i];
            if (nextNode != null) {
                String nextPrefix = prefix + (char)('a' + i);
                getWords(nextPrefix, words, nextNode);
            }
        }
    }

    /*
     ** Retrieve words contain prefix.
     */
    public ArrayList<String> retrieveWords(String prefix) {
        ArrayList<String> words = new ArrayList<>();
        Node currentNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            int j = prefix.charAt(i) - 'a';
            if (currentNode.children[j] == null) {
                return words;
            }
            currentNode = currentNode.children[j];
        }
        getWords(prefix,words,currentNode);
        return words;
    }
    /*
     ** Retrieve all words in dictionary.
     */
    public ArrayList<String> allWords() {
        ArrayList<String> words = new ArrayList<>();
        getWords("",words,root);
        return words;
    }

    // just for testing, never mind.
    public static void main(String[] args) {
        Trie testTrie = new Trie();
        testTrie.add("hi");
        testTrie.add("noway");
        testTrie.add("yes");
        testTrie.add("yesuo");
        testTrie.add("high");
        testTrie.add("highest");
        testTrie.add("higher");
        testTrie.delete("higher");
        testTrie.add("highest");
        ArrayList<String> words = testTrie.retrieveWords("high");
        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
        }
    }
}
