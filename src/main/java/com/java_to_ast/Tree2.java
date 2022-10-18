package com.java_to_ast;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import static com.github.javaparser.StaticJavaParser.*;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static java.util.stream.Collectors.toList;

public class Tree2 {
    Node root;
    // function l() which gives the leftmost child
    ArrayList<Integer> l = new ArrayList<>();
    // list of keyroots, i.e., nodes with a left child and the tree root
    ArrayList<Integer> keyroots = new ArrayList<>();
    // list of the labels of the nodes used for node comparison
    ArrayList<String> labels = new ArrayList<>();
    Map<Node, Integer> indexes = new HashMap<>();
    Map<Node, Node> leftmosts = new HashMap<>();


    // the following constructor handles preorder notation. E.g., f(a b(c))
    public Tree2(String s) throws IOException {
        Node tmp = parse(new FileInputStream(s));
        root = replace(tmp);
    }

    public Node getroot() {
        return root;
    }


    public void traverse() {
        // put together an ordered list of node labels of the tree
        traverse(root, "root", labels);
    }

    private static ArrayList<String> traverse(Node node, String name, ArrayList<String> labels) {
        assertNotNull(node);
        NodeMetaModel metaModel = node.getMetaModel();
        List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
        List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList)
                .collect(toList());

        StringBuilder nameBuilder = new StringBuilder(name);
        for (PropertyMetaModel a : attributes) {
            nameBuilder.append(a.getValue(node).toString());
        }
        name = nameBuilder.toString();
        for (PropertyMetaModel sn : subNodes) {
            Node nd = (Node) sn.getValue(node);
            if (nd != null) {

                labels = traverse(nd, sn.getName(), labels);

            }
        }
        for (PropertyMetaModel sl : subLists) {
            NodeList<? extends Node> nl = (NodeList<? extends Node>) sl.getValue(node);
            if (nl != null && nl.isNonEmpty()) {

                //builder.append(System.lineSeparator() + indent(level) + sl.getName() + ": ");
                String slName = sl.getName().substring(0, sl.getName().length() - 1);
                for (Node nd : nl) {

                    labels = traverse(nd, slName, labels);

                }
            }
        }
        labels.add(name);
        return labels;
    }

    public void index() {
        // index each node in the tree according to traversal method
        index(root, 0);
    }

    private int index(Node node, int index) {
        assertNotNull(node);
        NodeMetaModel metaModel = node.getMetaModel();
        List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
        List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList)
                .collect(toList());


        for (PropertyMetaModel sn : subNodes) {

            Node nd = (Node) sn.getValue(node);
            if (nd != null) {

                index(nd, index);

            }

        }

        for (PropertyMetaModel sl : subLists) {
            NodeList<? extends Node> nl = (NodeList<? extends Node>) sl.getValue(node);
            if (nl != null && nl.isNonEmpty()) {

                //builder.append(System.lineSeparator() + indent(level) + sl.getName() + ": ");
                //String slName = sl.getName().substring(0, sl.getName().length() - 1);
                for (Node nd : nl) {

                    index(nd, index);

                }
            }
        }
        //for (int i = 0; i < node.getChildNodes().size(); i++) {
        //  index = index(node.getChildNodes().get(i), index);
        //}
        index++;
        indexes.put(node, index);
        return index;
    }

    public void l() {
        // put together a function which gives l()
        leftmost();
        l = l(root, new ArrayList<Integer>());
    }

    private ArrayList<Integer> l(Node node, ArrayList<Integer> l) {
        for (int i = 0; i < node.getChildNodes().size(); i++) {
            l = l(node.getChildNodes().get(i), l);
        }
        //l.add(node.leftmost.index);
        if (indexes.get(leftmosts.get(node)) != null) {
            l.add(indexes.get(leftmosts.get(node)));
        }
        return l;
    }

    private void leftmost() {
        leftmost(root);
    }

    private void leftmost(Node node) {
        if (node == null)
            return;
        for (int i = 0; i < node.getChildNodes().size(); i++) {
            leftmost(node.getChildNodes().get(i));
        }
        if (node.getChildNodes().size() == 0) {
            leftmosts.put(node, node);
            //node.leftmost = node;
        } else {
            leftmosts.put(node, leftmosts.get(node.getChildNodes().get(0)));
            //node.leftmost = node.getChildNodes().get(0).leftmost;
        }
    }

    public void keyroots() {
        // calculate the keyroots
        for (int i = 0; i < l.size(); i++) {
            int flag = 0;
            for (int j = i + 1; j < l.size(); j++) {
                if (l.get(j).equals(l.get(i))) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                this.keyroots.add(i + 1);
            }
        }
    }

    static int[][] TD;

    public static int ZhangShasha(Tree2 tree1, Tree2 tree2) {
        tree1.index();
        tree1.l();
        tree1.keyroots();
        tree1.traverse();
        tree2.index();
        tree2.l();
        tree2.keyroots();
        tree2.traverse();

        ArrayList<Integer> l1 = tree1.l;
        ArrayList<Integer> keyroots1 = tree1.keyroots;
        ArrayList<Integer> l2 = tree2.l;
        ArrayList<Integer> keyroots2 = tree2.keyroots;

        // space complexity of the algorithm
        TD = new int[l1.size() + 1][l2.size() + 1];

        // solve subproblems
        for (int i1 = 1; i1 < keyroots1.size() + 1; i1++) {
            for (int j1 = 1; j1 < keyroots2.size() + 1; j1++) {
                int i = keyroots1.get(i1 - 1);
                int j = keyroots2.get(j1 - 1);
                TD[i][j] = treedist(l1, l2, i, j, tree1, tree2);
            }
        }

        return TD[l1.size()][l2.size()];
    }

    private static int treedist(ArrayList<Integer> l1, ArrayList<Integer> l2, int i, int j, Tree2 tree1, Tree2 tree2) {
        int[][] forestdist = new int[i + 1][j + 1];

        // costs of the three atomic operations
        int Delete = 1;
        int Insert = 1;
        int Relabel = 1;

        forestdist[0][0] = 0;
        for (int i1 = l1.get(i - 1); i1 <= i; i1++) {
            forestdist[i1][0] = forestdist[i1 - 1][0] + Delete;
        }
        for (int j1 = l2.get(j - 1); j1 <= j; j1++) {
            forestdist[0][j1] = forestdist[0][j1 - 1] + Insert;
        }
        for (int i1 = l1.get(i - 1); i1 <= i; i1++) {
            for (int j1 = l2.get(j - 1); j1 <= j; j1++) {
                int i_temp = (l1.get(i - 1) > i1 - 1) ? 0 : i1 - 1;
                int j_temp = (l2.get(j - 1) > j1 - 1) ? 0 : j1 - 1;
                if ((l1.get(i1 - 1).equals(l1.get(i - 1)))  && (l2.get(j1 - 1).equals(l2.get(j - 1)))) {

                    int Cost = (tree1.labels.get(i1 - 1).equals(tree2.labels.get(j1 - 1))) ? 0 : Relabel;
                    forestdist[i1][j1] = Math.min(
                            Math.min(forestdist[i_temp][j1] + Delete, forestdist[i1][j_temp] + Insert),
                            forestdist[i_temp][j_temp] + Cost);
                    TD[i1][j1] = forestdist[i1][j1];
                } else {
                    int i1_temp = l1.get(i1 - 1) - 1;
                    int j1_temp = l2.get(j1 - 1) - 1;

                    int i_temp2 = (l1.get(i - 1) > i1_temp) ? 0 : i1_temp;
                    int j_temp2 = (l2.get(j - 1) > j1_temp) ? 0 : j1_temp;

                    forestdist[i1][j1] = Math.min(
                            Math.min(forestdist[i_temp][j1] + Delete, forestdist[i1][j_temp] + Insert),
                            forestdist[i_temp2][j_temp2] + TD[i1][j1]);
                }
            }
        }
        return forestdist[i][j];
    }


    public static Node replace(Node cu) {
        String[][] arrayOfIndex;
        List<SimpleName> listOfVariable = cu.getChildNodesByType(SimpleName.class);
        HashMap<String,String> map = new HashMap<>();
        int i = 0;
        for (SimpleName a: listOfVariable){
            if (!map.containsKey(a.getIdentifier())){
                map.put(a.getIdentifier(), "v"+i);
                a.setIdentifier("v"+i);
                i++;
            }else{
                a.setIdentifier(map.get(a.getIdentifier()));
            }
        }
        return cu;
    }

    public static List<String> importfiles(Path dir, String s){
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*"+s+"*")) {
            for (Path entry: stream) {
                files.add(entry.toFile().toString());
            }
            return files;
        } catch (IOException x) {
            throw new RuntimeException(String.format("error reading folder %s: %s",
                    dir,
                    x.getMessage()),
                    x);
        }

    }
}