
package kadai2;
import java.sql.*;

public class Stockview{
	
	private Connection conn =null;
	
	//SQL接続
	public Stockview(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	//パラメータ確認(返り値はパラメータチェックフラグ)
	public boolean parcheak(String[] args) throws SQLException {
		
		boolean parflag = false;	//パラメータチェックフラグ(すべて問題ければ"True")
		final int ARGSCOUNT = 2; 	//argsの数
		
		Parcheak PC = new Parcheak();
		
		for (;;) {
			//パラメータの数を確認(１つはサブコマンドで確定、あってなければNG)
			/*if (args.length != ARGSCOUNT) {
				System.out.println("パラメータの数があっていません");
				break;
			}*/
			
			if (PC.argcheak(args,ARGSCOUNT) ==false) {
				System.out.println("パラメータの数があっていません。(サブコマンド込みで" + ARGSCOUNT +"つ必要です)");
				break;
			}
			
			parflag = true; //全パラメータが問題なければパラメータフラグをTrueにする
			break;
		}
		return parflag;
	}
	
	//在庫一覧を出力
	public void views(String args[]) throws SQLException{
		
		//在庫一覧を更新するSQL文
		String sql1 = "replace into stock (itemid,stockitems) "
					+ "select itemid,sum(items) from rack "
					+ "where itemid != 0 group by itemid";
		
		//在庫一覧を更新
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
	    stmt1.executeUpdate();
		stmt1.close();
		
		//棚番号基準("rackid")か、商品コード基準("itemid")かを判定()
		switch (args[1]) {
		case "rackid" ://棚番号基準
		
			//在庫一覧データを作成するSQL文（棚番号基準）
			String sql2 = "replace into stocklist (rackid,itemname,items) "
						+ "select rack.rackid,master.itemname,rack.items from rack,master "
						+ "where master.itemid = rack.itemid";
			
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
		    stmt2.executeUpdate();
			stmt2.close();
			
			//テーブルデータを出力(棚番号順に表示)
			String sql2_1 = "select * from stocklist order by rackid asc";
			PreparedStatement stmt2_1 = conn.prepareStatement(sql2_1);
			ResultSet rs2_1 = stmt2_1.executeQuery();
			
			System.out.println("-----------------------");
			System.out.println("|rackid|itemname|items|");
			System.out.println("-----------------------");
			while(rs2_1.next()) {
				System.out.println(rs2_1.getInt("rackid") + "   " +rs2_1.getString("itemname") + "   " +rs2_1.getInt("items"));
			}
			rs2_1.close();
			stmt2_1.close();
			
			break;
		
		case "itemid" ://商品コード基準
		
			//在庫一覧データを作成するSQL文（商品ID基準）
			String sql3 = "replace into stocklist2 (itemid,itemname,items) "
						+ "select master.itemid,master.itemname,stock.stockitems from master,stock "
						+ "where master.itemid = stock.itemid ";
			
			PreparedStatement stmt3 = conn.prepareStatement(sql3);
		    stmt3.executeUpdate();
			stmt3.close();
			
			//テーブルデータを出力(商品コード順に表示)
			String sql3_1 = "select * from stocklist2 where itemid != 0 order by itemid asc";
			PreparedStatement stmt3_1 = conn.prepareStatement(sql3_1);
			ResultSet rs3_1 = stmt3_1.executeQuery();
			
			System.out.println("-----------------------");
			System.out.println("|itemid|itemname|items|");
			System.out.println("-----------------------");
			while(rs3_1.next()) {
				System.out.println(rs3_1.getInt("itemid") + "   " 
						+rs3_1.getString("itemname") + "   " 
						+rs3_1.getInt("items"));
			}
			rs3_1.close();
			stmt3_1.close();
			break;
		
		//コマンドライン引数があってないならエラー処理			
		default :
			System.out.println("コマンドライン引数があってません(\"rackid\" or \"itemid\")");
			break;
		}	
	}
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
