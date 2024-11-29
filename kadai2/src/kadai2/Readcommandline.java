package kadai2;

import java.util.ArrayList;
import java.util.Arrays;

public class Readcommandline { 
	
	final private String subcommands[] = {"inputplan", "inputdata", "outputplan","outputdata",
										  "stock","item","inputview","outputview"};	//サブコマンド一覧
	private ArrayList<String> par = new ArrayList<>(); //サブコマンドを格納する配列
	
	//コマンドラインの内容を配列に格納
	public Readcommandline(String[] args) {
		for (int i =0 ; i <args.length;i++) {	
			par.add(args[i]);	
		}
	}
	
	//コマンドラインの数を出力
	public int parsize() {
		return par.size();
	}
	
	//コマンドラインの内容を出力
	public String parname (int p) {
		return par.get(p);
	}
	
	//サブコマンドの重複チェック
	public boolean subcommandcheak(String sc) {
		return Arrays.asList(subcommands).contains(sc);		
	}

}