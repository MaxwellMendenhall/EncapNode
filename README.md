[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=7514019&assignment_repo_type=AssignmentRepo)
# CSC 240 Computer Science III

### Homework 8 - Encapsulating Node with GPTree

[![Points badge](../../blob/badges/.github/badges/points.svg)](../../actions)

It’s time to plan ahead for the Genetic Programming project.  If we try to process more trees in our main() method, it will get unwieldy and hard to maintain.  A simplistic rule of thumb is that main() shouldn’t have more than about 20 lines of code in it.  It should make a few objects, and then tell them to "go about their business."

## Starting Out:

Since this project builds on earlier projects, we offer 2 ways to start this project:

You may copy the files from 
1. your completed Homework 7 to this directory, or
2. the [starter code repository](https://github.com/WCU-CS-CooperLab/CSC240-GPTree-Starter). 
    1. To use the starter code repository, you can either download the repository as a zip file, unzip the files and place them into this homowork repository or
    2. clone the repository and then copy the files from the cloned repository to this homework repository.

Whichever way you copy the files, only five of the copied files should be modified

1. `Node.java` - to add abstract method `traverse()`.
2. `Binop.java` - to add concrete method `traverse()`.
3. `Const.java` - to add concrete method `traverse()`.
4. `Variable.java` - to add concrete method `traverse()`.
5. `Divide.java` - to fix division by zero.

## The task

One way to add useful encapsulation is to create a GPTree class that consolidates some of the functionality of trees.  Currently, the Node class and its subclasses know how to do things, but a tree class that encapsulates the root node of a tree will be useful.  This process has five steps. 


**Step 1** The `Node` class will be modified to have an abstract `traverse(Collector c)` method that will recursively and give Nodes in the Tree to the `Collector` interface to collect them. That way, a list of potential `Binop` Nodes will be held by the `GPTree` to be randomly selected as crossover points.

**Step 2** You will add 3 new files to this project, `Collector.java`, `GPTree.java` and `TestGPTree.java` described below.

File 1: The `Collector` interface will have one method called `collect(Node node)` as shown here:

```java
public interface Collector {
    void collect(Node node);
}
``` 
   
File 2: The GPTree class will implement the `Collector` interface to collect Nodes appropriate for Crossover. You will write this implementation in Step 3. In addition to implementing `Collector, the GPTree class encapsulates the Algebra Tree and implements a `crossover()` method: 


```java
public class GPTree implements Collector {
    private Node root;
    private ArrayList<Binop> crossNodes;
    
    
    /**
     * @param - node The node to be collected.
     * TODO: implement this method
     */
    public void collect(Node node) {
        // add code Node to crossNodes if it is a Binop
        
    }
    
    
    // DO NOT EDIT code below for Homework 8. 
    // If you are doing the challenge mentioned in 
    // the comments above the crossover method
    // then you should create a second crossover
    // method above this comment with a slightly 
    // different name that handles all types
    // of crossover.
    
    
    /**
     * This initializes the crossNodes field and
     * calls the root Node's traverse method on this
     * so that this can collect the Binop Nodes.
     */
    public void traverse() {
        crossNodes = new ArrayList<Binop>();
        root.traverse(this);
    }
    
    
   
    
    /**
     * this implements left child to left child
     * and right child to right child crossover.
     * Challenge: additionally implement left to 
     * right child and right to left child crossover.
     */
    public void crossover(GPTree tree, Random rand) {
        // find the points for crossover
        this.traverse();
        tree.traverse();
        int thisPoint = rand.nextInt(this.crossNodes.size());
        int treePoint = rand.nextInt(tree.crossNodes.size());
        boolean left = rand.nextBoolean();
        // get the connection points
        Binop thisTrunk = crossNodes.get(thisPoint);
        Binop treeTrunk = tree.crossNodes.get(treePoint);
        if(left) {
            // save the branches 
            Node thisBranch = thisTrunk.lChild;
            Node treeBranch = treeTrunk.lChild;
            // swap the branches
            thisTrunk.lChild = treeBranch;
            treeTrunk.lChild = thisBranch;
        } else {
            // save the branches
            Node thisBranch = thisTrunk.rChild;
            Node treeBranch = treeTrunk.rChild;
            // swap the branches
            thisTrunk.rChild = treeBranch;
            treeTrunk.rChild = thisBranch;
        }
        
    }

    GPTree() { 
        root = null; 
    }    
    
    GPTree(OperatorFactory o, TerminalFactory t, int maxDepth, Random rand) {
        root = o.getOperator(rand);
        root.addRandomKids(o, t, maxDepth, rand);
    }
    
    public String toString() { 
        return root.toString(); 
    }
    
    public double eval(double[] data) { 
        return root.eval(data); 
    }
    
}
```

File 3: TestGPTree will be your test class:

```java
import java.util.*;

public class TestGPTree {
    static int numIndepVars = 3;
    static int maxDepth = 5;
    static Random rand = new Random();
    public static void main(String[] args) {
        double[] data = {3.14, 2.78, 1.0};
        Node[] ops = {new Plus(), new Minus(), new Mult(), new Divide()};
        OperatorFactory o = new OperatorFactory(ops);
        TerminalFactory t = new TerminalFactory(numIndepVars);
        GPTree gpt1 = new GPTree(o, t, maxDepth, rand);
        System.out.println(gpt1 + " = " + gpt1.eval(data));
        GPTree gpt2 = new GPTree(o, t, maxDepth, rand);
        System.out.println(gpt2 + " = " + gpt2.eval(data));
        gpt1.crossover(gpt2,rand);
        System.out.println("After crossover");
        System.out.println(gpt1 + " = " + gpt1.eval(data));
        System.out.println(gpt2 + " = " + gpt2.eval(data));
    }
}
```

The `main()` method of the test class now tests the `GPTree` class as well as new versions using crossover,  and it is ready to be extended to compute a fitness and have real child `GPTree`s.

**Step 3** Implement the `collect()` method in `GPTree` so that any `Node` that is a `Binop` is added to the `crossNodes` `ArrayList` for use in crossover.


**Step 4** Next, the `Binop`, `Const`, and `Variable` classes will need to add a `traverse()` method that calls collect on itself and recursively `traverse()` through any child nodes. Traversal of a tree is defined as *visiting* each node of a tree. In our case *visiting* means having the collector collect the `Node` that is being traversed. 


## Binop

In `Binop`:

```java
/**
 * collect using preorder traversal.
 */
public void traverse(Collector c) {
    // collect this
    
    // traverse lChild
    
    // traverse rChild
    
}   
```

## Const and Variable

In `Const` and `Variable`:

```java
public void traverse(Collector c) {
    // collect this
    
}   
```

**Step 5** And, finally, `Divide` will need to be changed to deal with potential `Infinity` values in `eval()`

## Divide

If you run your program many times, you might see that a GPtree that evaluates to Infinity, if a divisor in a Divide class object evaluates to zero.  To handle this case, modify the `Divide` class so that if the divisor is less than 0.0001 in absolute value, the numerical `eval()` method simply returns the value 1.0. I know this seems counter-intuitive, but just do it.

