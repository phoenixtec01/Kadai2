package kadai2;
import java.sql.*;

public class Inputplan {

	//private final String SQL = "select * from employee where id =? ;";
	private PreparedStatement stmt =null;
	private PreparedStatement stmt2 =null;
	private final int ITEMID = 1;
	private final int ITEMS = 2;
	private final int PLANDAY = 3;
	private final int CODE = 4;
	private final int STATUS = 5;
	private String code;
	private Connection conn;
	public Inputplan(String URL) {

		try {
			conn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	public boolean parametercheak(String[] args) {

		return true;
	}
	
	private boolean codecheak(){
		/*			String sql = "SELECT * FROM inputplan WHERE inputcode = ?";
		stmt = conn.prepareStatement(sql);
		stmt.setInt(1,code);

		ResultSet rs = stmt.executeQuery();
		int x = 0;
		while(rs.next()) {
			 x = rs.getInt("inputcode") ;
		}
		
	    stmt.close();
		System.out.println(x);
	    */
		return true;
	}
	
	public void datainput(int code,int itemid,int items,int planday) {
		try {
			String sql2 = "INSERT INTO inputplan VALUES( ?, ?, ?, ?, ?) ";
			stmt2 = conn.prepareStatement(sql2);
		    stmt2.setInt(ITEMID, itemid);
		    stmt2.setInt(ITEMS, items);
		    stmt2.setInt(PLANDAY, planday);
		    stmt2.setInt(CODE, code);
		    stmt2.setInt(STATUS, 0);
		    
		    conn.setAutoCommit(false);
            try {
                // SQLの実行(INSERT文、UPDATE文、DELETE文でも可能)
               stmt2.executeUpdate();
 
                // コミット
                conn.commit();
            }catch (Exception e) {
 
                // ロールバック
                conn.rollback();
 
                System.out.println("データの更新に失敗しました。");
            }
            stmt2.close();

            conn.close(); 

	}catch (SQLException e) {
		// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
