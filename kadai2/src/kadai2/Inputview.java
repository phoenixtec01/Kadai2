/**
 * Inputview未実装
 */
package kadai2;

import java.sql.*;

/**
 * 
 */
public class Inputview { //未実装
	private Connection conn =null;
	
	public Inputview(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	/* 入荷一覧データ (inputlist)
	    - 商品名、入荷予定数、入荷予定日、予約コード、入荷ステータス、更新バージョン
	
	
	
	　コマンドラインの引数で表示内容を決める。
		1. now →最新バージョン
		2. 
	*/
	public void views() throws SQLException{
		
	}
	//SQL切断
		public void sqlclose() throws SQLException {
			if (conn !=null) {
				conn.close();
			}
		}
}
