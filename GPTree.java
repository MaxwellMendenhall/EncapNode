import java.util.ArrayList;
import java.util.Random;

public class GPTree implements Collector {
	
	private Node root;
    private ArrayList<Binop> crossNodes;
    
	@Override
	public void collect(Node node) {
		//adds node to crossNodes if it is Binop
		//System.out.println("Node instanceof Binop: " + (node instanceof Binop));
		if(node instanceof Binop) {
			crossNodes.add((Binop) node);
		}else {
			//System.out.println("The node is not Binop, nothing is added to crossNodes");
		}
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
