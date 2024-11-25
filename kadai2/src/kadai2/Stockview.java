/**
 * 
 */
package kadai2;
import java.sql.*;
/**
 * 
 */
public class Stockview {
	private Connection conn =null;
	
	public Stockview(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	public void views() throws SQLException{
		
		//在庫一覧を更新するSQL
		String sql1 ="replace into stock (itemid,stockitems) "
				+ "select itemid,sum(items) "
				+ "from rack group by itemid";
		
		//在庫一覧を更新
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
	    stmt1.executeUpdate();
		stmt1.close();
		
		//在庫一覧データを表示するSQL（棚番号基準）
		String sql2 = "insert into stocklist (rackid,itemname,items) select rack.rackid,master.itemname,rack.items "
				+ "from rack,master where rack.itemid = master.itemid";
		
		//在庫一覧データを表示するSQL（商品ID基準）
		String sql3 = "insert into stocklist2 (itemid,itemname,items) select master.itemid,master.itemname,stock.stockitems "
				+ "from master,stock where master.itemid = stock.itemid;";
		
		
	}
	
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
