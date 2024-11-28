/**
 * 
 */
package kadai2;
import java.sql.*;
/**
 * 
 */
public class Stockview extends Parcheak{
	private Connection conn =null;
	
	public Stockview(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	public boolean parcheak(String[] args) throws SQLException {
		boolean parflag = false;

		final int ARGSCOUNT = 2; //argsの数
		
		for (;;) {
			/*if (args.length != ARGSCOUNT) {	//パラメータの数をカウント(１つはサブコマンドで確定)
				System.out.println("パラメータの数があっていません");
				break;
			}*/
			
			if (super.Parcheak(args,ARGSCOUNT) ==false) {
				break;
			}
			
			parflag = true;
			break;
		}
		return parflag;
	}
			
	public void views(String args[]) throws SQLException{
		
		//在庫一覧を更新するSQL
		String sql1 ="replace into stock (itemid,stockitems) "
				+ "select itemid,sum(items) where itemid != 0 "
				+ "from rack group by itemid";
		
		//在庫一覧を更新
		PreparedStatement stmt1 = conn.prepareStatement(sql1);
	    stmt1.executeUpdate();
		stmt1.close();
		
		switch (args[1]) {
		case "rack" :
		
			//在庫一覧データを作成するSQL（棚番号基準）
			String sql2 = "replace into stocklist (rackid,itemname,items) "
					+ "select rack.rackid,master.itemname,rack.items from rack,master "
					+ "where  master.itemid = rack.itemid";
			
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
		    stmt2.executeUpdate();
			stmt2.close();
			
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
		
		case "item" :
		
			//在庫一覧データを作成するSQL（商品ID基準）
			String sql3 = "replace into stocklist2 (itemid,itemname,items) "
					+ "select master.itemid,master.itemname,stock.stockitems from master,stock "
					+ "where master.itemid = stock.itemid ";
			
			PreparedStatement stmt3 = conn.prepareStatement(sql3);
		    stmt3.executeUpdate();
			stmt3.close();
			
			String sql3_1 = "select * from stocklist2 order by itemid asc";
			PreparedStatement stmt3_1 = conn.prepareStatement(sql3_1);
			ResultSet rs3_1 = stmt3_1.executeQuery();
			
			System.out.println("-----------------------");
			System.out.println("|itemid|itemname|items|");
			System.out.println("-----------------------");
			while(rs3_1.next()) {
				System.out.println(rs3_1.getInt("itemid") + "   " +rs3_1.getString("itemname") + "   " +rs3_1.getInt("items"));
			}
			rs3_1.close();
			stmt3_1.close();
			break;
		
		default :
			System.out.println("コマンドライン引数があってません(\"rack\" or \"item\")");
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
