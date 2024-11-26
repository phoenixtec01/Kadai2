/**
 * 
 */
package kadai2;

import java.sql.*;

/**
 * 
 */
public class Inputview {
	private Connection conn =null;
	
	public Inputview(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	public void views() throws SQLException{
		
	}
	//SQL切断
		public void sqlclose() throws SQLException {
			if (conn !=null) {
				conn.close();
			}
		}
}
