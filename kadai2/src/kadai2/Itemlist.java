
package kadai2;
import java.sql.*;

public class Itemlist {
	
	private Connection conn =null;
	
	//SQL接続	
	public Itemlist(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	/*商品一覧データ (itemlist (itemid,itemname,inputitems,outputitems,stockitems,reserveitems))
    - 	商品コード		master.itemid
    	商品名		master.itemname
    	入荷予定数	inputplan.inputitems 
    	出荷予定数	outputlist.outputitems 
    	在庫数		stock.stockitems
    	引当可能数	stock.stockitems - outputplan.outputitems 
    	を出力
	*/
	public void views() throws SQLException{ 
		
		//商品一覧を出力するSQL文(外部結合で他テーブルの内容を結合する。)
		String sql1 = "replace into itemlist(itemid,itemname,inputitems,outputitems,stockitems,reserveitems) " 				//商品一覧テーブルを更新
					+ "select master.itemid,master.itemname,"								//マスターデータの商品コードと商品IDを読み込む
																							//(以下のデータはテーブル読み込み時にデータがない(Null)場合、数を0とする。)
					+ "ifnull(inputplan.inputitems,0),"										//入荷予定データから入荷予定数を読み込む。
					+ "ifnull(outputplan.outputitems,0),"									//出荷予定データから出荷予定数を読み込む。
					+ "ifnull(stock.stockitems,0),"											//在庫データから在庫数を読み込む。
					+ "ifnull(stock.stockitems - outputplan.outputitems,0) from master "	//在庫データと出荷予定数から引当可能数を計算する。
																						
					+ "left outer join inputplan on master.itemid = inputplan.itemid and inputplan.inputstatus = 0 "		//外的結合でinputplanテーブルを結合
					+ "left outer join outputplan on master.itemid = outputplan.itemid and outputplan.outputstatus = 0 "	//外的結合でoutputplanテーブルを結合
					+ "left outer join stock on master.itemid = stock.itemid";												//外的結合でstockテーブルを結合
			
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
	    stmt1.executeUpdate();
		stmt1.close();
		
		//テーブルデータを出力(商品コード順に表示)
		String sql1_1 = "select * from itemlist order by itemid asc";
		PreparedStatement stmt1_1 = conn.prepareStatement(sql1_1);
		ResultSet rs1_1 = stmt1_1.executeQuery();
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("|itemid|itemname|inputitems|outputitems|stockitems|reserveitems|");
		System.out.println("----------------------------------------------------------------");
		while(rs1_1.next()) {
			System.out.println(rs1_1.getInt("itemid") + "   " 
							+rs1_1.getString("itemname") + "   " 
							+rs1_1.getInt("inputitems")+ "   " 
							+rs1_1.getInt("outputitems")+ "   " 
							+rs1_1.getInt("stockitems")+ "   " 
							+rs1_1.getInt("reserveitems"));
		}
		rs1_1.close();
		stmt1_1.close();
	}
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
