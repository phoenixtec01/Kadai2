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
				}
				ID.sqlclose();
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
			System.out.println("subcommand finished.");	
		}catch (Exception e){
			System.out.println("Error");
	}
	System.out.println("Program finished.");
	}
}
