package kadai2;
import java.sql.*;

public class main {
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		final String URL = "jdbc:sqlite:C:\\Users\\meets\\Documents\\Kadai2\\kadai2\\database.db";
		final int SUBCOMMAND_PAR = 0;
		final int PAR1 = 1;
		final int PAR2 = 2;
		final int PAR3 = 3;
		final int PAR4 = 4;
		
		String subcommand; //サブコマンド
		int data[] = new int[4];
		int code; // 予約コード
		int itemid; //商品ID
		int items; //予定数
		int planday; //予定日
		try {
			Readcommandline Rcline = new Readcommandline(args);
			subcommand = Rcline.parname(SUBCOMMAND_PAR);
				if (Rcline.subcommandcheak(subcommand) ==false) {
					System.out.println("subcommand Error!");
					return ;
				}
			switch (subcommand){
			case "inputplan":
				code =Integer.parseInt(Rcline.parname(PAR1));
				itemid = Integer.parseInt(Rcline.parname(PAR2));
				items =Integer.parseInt(Rcline.parname(PAR3));
				planday = Integer.parseInt(Rcline.parname(PAR4));
				
				Inputplan IP = new Inputplan(URL);
				
				IP.datainput(code, itemid, items, planday);
				
				System.out.println("inputplan finished");
				break;
			case "inputdata":
				break;
			case "outputplan":
				break;
			case "outputdata":
				break;
			case "stock":
				break;
			case "item":
				break;
			case "inputview":
				break;
			case "outputview":
				break;
			}
			System.out.println("subcommand finished");	
		}catch (Exception e){
			System.out.println("Error");
	}
	System.out.println("finished.");
	}
}
