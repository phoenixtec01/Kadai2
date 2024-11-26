/**
 * 
 */
package kadai2;
import java.sql.*;
/**
 * 
 */
public class Itemlist {
	private Connection conn =null;
	
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
	public void views() throws SQLException{ //外部結合で他テーブルの内容を結合する。このとき、Nullの場合は0を入力する。
		String sql1 = "replace into itemlist(itemid,itemname,inputitems,outputitems,stockitems,reserveitems) "
				+ "select master.itemid,master.itemname,ifnull(inputplan.inputitems,0),ifnull(outputplan.outputitems,0),"
				+ "ifnull(stock.stockitems,0),ifnull(stock.stockitems - outputplan.outputitems,0) from master "
				+ "left outer join inputplan on master.itemid = inputplan.itemid and inputplan.inputstatus = 0 "
				+ "left outer join outputplan on master.itemid = outputplan.itemid and outputplan.outputstatus = 0 "
				+ "left outer join stock on master.itemid = stock.itemid";
		
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
	    stmt1.executeUpdate();
		stmt1.close();
		
		String sql1_1 = "select * from itemlist order by itemid asc";
		PreparedStatement stmt1_1 = conn.prepareStatement(sql1_1);
		ResultSet rs1_1 = stmt1_1.executeQuery();
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("|itemid|itemname|inputitems|outputitems|stockitems|reserveitems|");
		System.out.println("----------------------------------------------------------------");
		while(rs1_1.next()) {
			System.out.println(rs1_1.getInt("itemid") + "   " +rs1_1.getString("itemname") + "   " 
					+rs1_1.getInt("inputitems")+ "   " +rs1_1.getInt("outputitems")+ "   " 
					+rs1_1.getInt("stockitems")+ "   " +rs1_1.getInt("reserveitems"));
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
