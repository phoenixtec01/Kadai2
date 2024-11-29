
package kadai2;
import java.sql.*;

 class Parcheak { //パラメータチェック一覧

	public Parcheak() {
		
	}
	
	//パラメータの数を確認(１つはサブコマンドで確定、あってなければNG)
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
		
		String itemid; //SQLで出力した商品コード
		
		//指定した商品コードを出力するSQL文
		String sql = " select itemid from master where itemid = ?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1,id);
		ResultSet rs = stmt.executeQuery();
		itemid = rs.getString("itemid");//商品コードが存在してなかったらnullを返す
		rs.close();
		stmt.close();
		if (itemid != null) {//商品コードが存在してなかったらnullを返す
			return true;//商品コードが重複している
		}else {
			return false;//商品コードが重複していない
		}
	}
	
	//予約コードの重複or存在確認
	public boolean dupcodecheak(String code,Connection conn,String mode) throws SQLException {
		
		String IOcode;//SQLで出力した予約コード
		
		/*指定した予約コードを出力するSQL文
		※入荷時か出荷時かで必要なSQL文が異なるため、両社のSQL文の共通部分と各クラスの引数を組み合わせて1つのSQL文を作成する。
		最終的に作成されるSQL文は
		入荷時："select * from inputplan where inputcode = ?"
		出荷時:"select * from outputplan where outputcode = ?"　になる。*/
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
			return true;//予約コードが重複していない or 存在していない
		}else {
			return false;//予約コードが重複している or 存在している
		}
	}
	
	//入荷(出荷)ステータス確認
	public boolean statuscheak(String code,Connection conn,String mode) throws SQLException {
		
		int IOStatus;//SQLで出力したステータス
		
		/*指定した予約コードの入(出)荷ステータスを出力するSQL
		※入荷時か出荷時かで必要なSQL文が異なるため、両社のSQL文の共通部分と各クラスの引数を組み合わせて1つのSQL文を作成する。
		最終的に作成されるSQL文は
		入荷時："select * from inputplan where inputcode = ? and inputstatus = 1"
		出荷時:"select * from outputplan where outputcode = ? and outputstatus = 1"　になる。*/
		
		String sqla = "select * from ";
		String sqlb = "plan where ";
		String sqlc = "code = ? and ";
		String sqld = "status = 1";
		String sql = sqla + mode + sqlb + mode + sqlc + mode + sqld;
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1,code);
		ResultSet rs = stmt.executeQuery();
		IOStatus =rs.getInt(mode +"status");//入荷（出荷）していなければ0、入荷（出荷）済みなら1
		rs.close();
		stmt.close();
		if (IOStatus == 0) {
			return true; //入荷（出荷）未
		}else {
			return false; //入荷（出荷）済
		}
	}
	
}
