public class Divide extends Binop{

	public Divide(Node l, Node r) {
		super(l, r);
	}
	public Divide() {
		
	}
	public String toString() {
		return "(" + lChild.toString() + " / " + rChild.toString() + ")";
	}

	@Override
	public double eval(double[] values) {
		double a = lChild.eval(values) / rChild.eval(values);
		try {
			if(a == Double.POSITIVE_INFINITY || a == Double.NEGATIVE_INFINITY){
				throw new ArithmeticException();
			}else {
				return a;
			}
		}catch(ArithmeticException e){
			return 1.0;
		}
	}

}
