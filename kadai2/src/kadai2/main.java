package kadai2;

public class main {
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		final String URL = "jdbc:sqlite:C:\\Users\\meets\\Documents\\Kadai2\\kadai2\\database.db"; //データベースのURL
		final int SUBCOMMAND_PAR = 0;//(コマンドラインが格納された配列における)サブコマンドの格納位置
		String subcommand; //サブコマンド

		try {
			Readcommandline Rcline = new Readcommandline(args); //コマンドラインの内容を配列に格納
			subcommand = Rcline.parname(SUBCOMMAND_PAR);//コマンドラインの1番目(サブコマンド)を出力
			if (Rcline.subcommandcheak(subcommand) ==false) {//サブコマンドが指定されたコマンドであるか確認
				System.out.println("subcommand Error!");
				return;
			}
			switch (subcommand){//サブコマンドごとに分岐
			case "inputplan"://入荷予定入力
				Inputplan IP = new Inputplan(URL); //SQL接続
				if(IP.Parcheak(args)==false) { //パラメータ確認
					System.out.println("parameter error!");
				}else { 
					IP.datainput();//データ記入
					System.out.println("inputplan finished.");
				}
				IP.sqlclose();
				break;
				
			case "inputdata"://入荷入力
				Inputdata ID = new Inputdata(URL);//SQL接続
				if(ID.parcheak(args)==false) {//パラメータ確認
					System.out.println("parameter error!");	
				} else {
					ID.datainput();
					System.out.println("Inputdata finished.");
				}
				ID.sqlclose();
				break;
				
			case "outputplan"://出荷予定入力
				Outputplan OP = new Outputplan(URL); //SQL接続
				if(OP.parcheak(args)==false) { //パラメータ確認
					System.out.println("parameter error!");
				}else { 
					OP.datainput();//データ記入
					System.out.println("Outputplan finished.");
				}
				OP.sqlclose();
				break;
				
			case "outputdata"://出荷入力
				Outputdata OD = new Outputdata(URL); //SQL接続
				if(OD.parcheak(args)==false) {//パラメータ確認
					System.out.println("parameter error!");	
				} else {
					OD.datainput();//データ記入
					System.out.println("Outputdata finished.");
				}
				OD.sqlclose();
				break;
				
			case "stock": //在庫一覧出力
				Stockview SV = new Stockview(URL); //SQL接続
				if(SV.parcheak(args)==false) {//パラメータ確認
					System.out.println("parameter error!");	
				} else {
					SV.views(args);//データ出力
					System.out.println("Stockview finished.");
				}
				SV.sqlclose();
				break;
			case "item": //商品一覧出力
				Itemlist IL = new Itemlist(URL);
				IL.views();//データ出力
				
				IL.sqlclose();
				break;
			case "inputview"://入荷一覧出力(未実装)
				break;
			case "outputview"://出荷一覧出力(未実装)
				break;
			}
			System.out.println("subcommand finished.");	
		}catch (Exception e){
			System.out.println("Error");
	}
	System.out.println("Program finished.");
	}
}
