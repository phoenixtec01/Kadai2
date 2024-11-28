/**
 * 
 */
package kadai2;
import java.sql.*;
/**

 */
 class Parcheak {

	public Parcheak() {
		
	}
	
	//パラメータの数をカウント(１つはサブコマンドで確定)
	public boolean argcheak(String[] args,int argcount) {
		
		if (args.length != argcount) {	
			return false;
		}
		return true;
	}
	
	//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
	public boolean codecheak(String inputcode,int codecount) {
		
		if (inputcode.length() != codecount) {
			return false;
		}
		return true;
	}
	
	//予定数の内容チェック(予定数は1以上なので、0未満はNG)
	public boolean itemcheak(int items) {
		
		if (items < 1) {
			return false;
		}
		return true;
	}
	
	//日付の内容チェック(日付は8桁表示なので、8桁ないとNG)
	public boolean daycheak(String day) {
		
		if (day.length() != 8 ) {
			return false;
		}
		return true;
	}
	
	//商品コードのチェック
	public boolean itemidcheak(int id,Connection conn) throws SQLException {
		
		String itemid;
		String sql0 = " select itemid from master where itemid = ?";
		PreparedStatement stmt0 = conn.prepareStatement(sql0);
		
		stmt0.setInt(1,id);
		ResultSet rs0 = stmt0.executeQuery();
		itemid = rs0.getString("itemid");//商品コードが存在してなかったらnullを返す
		rs0.close();
		stmt0.close();
		if (itemid != null) {//商品コードが存在してなかったらnullを返す
			return true;//コードが重複している
		}else {
			return false;//コードが重複していない
		}
	}
	
	//予約コードの重複or存在確認
	public boolean dupcodecheak(String code,Connection conn,String mode) throws SQLException {
		
		String IOcode;
		String sqla = "select * from ";
		String sqlb = "plan where ";
		String sqlc	= "code = ?";
		String sql = sqla + mode + sqlb + mode + sqlc;
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1,code);
		ResultSet rs = stmt.executeQuery();
		IOcode = rs.getString(mode + "code");//コードが重複してなかったらnullを返す
		rs.close();
		stmt.close();
		if (IOcode == null) {
			return  true;//コードが重複していない or 存在していない
		}else {
			return false;//コードが重複している or 存在している
		}
	}
	
}
