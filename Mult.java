public class Mult extends Binop{

	public Mult(Node l, Node r) {
		super(l, r);
	}
	public Mult() {
		
	}

	public String toString() {
		return "(" + lChild.toString() + " * " + rChild.toString() + ")";
	}

	@Override
	public double eval(double[] values) {
		return lChild.eval(values) * rChild.eval(values);
	}
}
