package kadai2;

public class main {
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		final String URL = "jdbc:sqlite:C:\\Users\\meets\\Documents\\Kadai2\\kadai2\\database.db";
		final int SUBCOMMAND_PAR = 0;
		String subcommand; //サブコマンド

		try {
			Readcommandline Rcline = new Readcommandline(args);
			subcommand = Rcline.parname(SUBCOMMAND_PAR);
			if (Rcline.subcommandcheak(subcommand) ==false) {
				System.out.println("subcommand Error!");
				return;
			}
			switch (subcommand){
			case "inputplan":
				
				Inputplan IP = new Inputplan(URL); //SQL接続
				
				if(IP.parcheak(args)==false) { //パラメータ確認
					System.out.println("parameter error!");
				}else { //データ記入
					IP.datainput();
					System.out.println("inputplan finished.");
				}
				IP.sqlclose();
				break;
				
			case "inputdata":
				
				Inputdata ID = new Inputdata(URL);
				if(ID.parcheak(args)==false) {
					System.out.println("parameter error!");	
				} else {
					ID.datainput();
					System.out.println("Inputdata finished.");
				}
				ID.sqlclose();
				break;
				
			case "outputplan":
				
				Outputplan OP = new Outputplan(URL); //SQL接続
				
				if(OP.parcheak(args)==false) { //パラメータ確認
					System.out.println("parameter error!");
				}else { //データ記入
					OP.datainput();
					System.out.println("Outputplan finished.");
				}
				OP.sqlclose();
				break;
				
			case "outputdata":
				Outputdata OD = new Outputdata(URL);
				
				if(OD.parcheak(args)==false) {
					System.out.println("parameter error!");	
				} else {
					OD.datainput();
					System.out.println("Outputdata finished.");
				}
				OD.sqlclose();
				break;
				
			case "stock"://仕掛中
				Stockview SV = new Stockview(URL);
				
				SV.sqlclose();
				break;
			case "item":
				break;
			case "inputview":
				break;
			case "outputview":
				break;
			}
			System.out.println("subcommand finished.");	
		}catch (Exception e){
			System.out.println("Error");
	}
	System.out.println("Program finished.");
	}
}
