package kadai2;

import java.util.ArrayList;
import java.util.Arrays;

public class Readcommandline {
	
	final private String subcommands[] = {"inputplan", "inputdata", "outputplan","outputdata","stock","item","inputview","outputview"};	
	private ArrayList<String> par = new ArrayList<>();
	
	public Readcommandline(String[] args) {
		
		for (int i =0 ; i <args.length;i++) {	
				par.add(args[i]);
				System.out.println(args[i]);	
			}
		}
	
	public int parsize() {
		return par.size();
	}
	
	
	public String parname (int p) {
		return par.get(p);
	}
	

	public boolean subcommandcheak(String sc) {
		return Arrays.asList(subcommands).contains(sc);		
	}

}