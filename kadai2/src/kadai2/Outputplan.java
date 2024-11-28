/**
 * 
 */
package kadai2;
import java.sql.*;
/**
 * 
 */
public class Outputplan {
	
	private Connection conn =null;
	private int outputitemid; //商品コード
	private int outputitems; //予定数
	private String outputplanday; //予定日
	private String outputcode; //予約コード
	
	//SQL接続
	public Outputplan(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	//パラメータ確認
	public boolean parcheak(String[] args) throws SQLException {
		boolean parflag = false;
		final int CODECOUNT = 8; //予約コードの文字数
		final int ARGSCOUNT = 5; //argsの数
		Parcheak PC = new Parcheak();
		final String mode = "output";//ParcheakクラスでSQL文を作成する際に使用する構成文の差異部分(outputplanとoutputcodeを作成する)
		
		for (;;) {
			/*if (args.length != ARGSCOUNT) {	//パラメータの数をカウント(１つはサブコマンドで確定)
				System.out.println("パラメータの数があっていません");
				break;
			}*/
			
			if (PC.argcheak(args,ARGSCOUNT) ==false) {
				break;
			}
			

			//パラメータの内容を格納
			int index = 1;
			outputcode =args[index++];
			outputitemid = Integer.parseInt(args[index++]);
			outputitems =Integer.parseInt(args[index++]);
			outputplanday = args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			
			/*if (outputcode.length() != CODECOUNT) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}*/
			
			if (PC.codecheak(outputcode, CODECOUNT) ==false) {
				break;
			}

			//商品コードの内容チェック(商品コードが登録されていない商品や商品コードが0の場合はNG)
			/*if (itemcheak(outputitemid) ==false || outputitemid ==0) {
				System.out.println("商品コードの値が不正です");
				break;
			}*/
			
			if(PC.itemidcheak(outputitemid, conn) == false || outputitemid ==0) {
				System.out.println("商品コードの値が不正です");
				break;
			}
			//予定数の内容チェック(予定数は1以上なので、0未満はNG)
			/*if (outputitems < 1) {
				System.out.println("予定数の値が不正です");
				break;
			}*/
			
			if (PC.itemcheak(outputitems) ==false) {
				break;
			}
			
			//日付の内容チェック(日付は8桁表示なので、8桁ないとNG)
			/*if (outputplanday.length() != 8 ) {
				System.out.println("日付の値が不正です");
				break;
			}*/
			
			if (PC.daycheak(outputplanday) ==false) {
				break;
			}
			
			//予約コードの重複確認(outputplanデータ内で重複したらNG)
			/*if (codecheak(outputcode) == false) {
				System.out.println("予約コードが重複しています");
	    		break;
			}*/
			
			if(PC.dupcodecheak(outputcode, conn, mode) == false) {
				System.out.println("予約コードが重複しています");
	    		break;
			}
			
			parflag = true;
			break;
		}
		return parflag;
	}
	//商品コードチェック(Parcheakクラスで実装済み)
	/*private boolean itemcheak(int id) throws SQLException{
		boolean itemflag = false;

		String sql0 = " select itemid from master where itemid = ?";
		PreparedStatement stmt0 = conn.prepareStatement(sql0);
		
		stmt0.setInt(1,id);
		ResultSet rs0 = stmt0.executeQuery();
		
		if (rs0.getString("itemid") != null) {//商品コードが存在しなかったらしてなかったらnullを返す
			itemflag = true;
		}else {
			itemflag = false;
		}
		
		return itemflag;
	}*/
		
	//予約コード重複確認(Parcheakクラスで実装済み)
	/*private boolean codecheak(String code) throws SQLException{
		boolean codeflag = false;
		String sql = "select * from outputplan where outputcode = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1,code);
		ResultSet rs = stmt.executeQuery();
		if (rs.getString("outputcode") == null) {//コードが重複してなかったらnullを返す
			codeflag = true;
		}else {
			codeflag = false;
		}
		rs.close();
		stmt.close();
		return codeflag;
	}*/
	
	//データ記入
	public void datainput() throws SQLException {
		final int ITEMID = 1; 
		final int ITEMS = 2;
		final int PLANDAY = 3;
		final int CODE = 4;
		final int STATUS = 5;
		String sql2 = "INSERT INTO outputplan VALUES( ?, ?, ?, ?, ?) ";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		
	    stmt2.setInt(ITEMID, outputitemid);
	    stmt2.setInt(ITEMS, outputitems);
	    stmt2.setString(PLANDAY, outputplanday);
	    stmt2.setString(CODE, outputcode);
	    stmt2.setInt(STATUS, 0);
	    stmt2.executeUpdate();
	    
        stmt2.close();
	}
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}


