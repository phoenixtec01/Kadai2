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
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
