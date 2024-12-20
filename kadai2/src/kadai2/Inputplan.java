
package kadai2;
import java.sql.*;

public class Inputplan{

	private Connection conn =null;
	private int inputitemid; 		//商品コード
	private int inputitems; 		//予定数
	private String inputplanday; 	//予定日
	private String inputcode; 		//予約コード
	
	//SQL接続
	public Inputplan(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);	
	}
	
	//パラメータ確認(返り値はパラメータチェックフラグ)
	public boolean parcheak(String[] args) throws SQLException {
		
		boolean parflag = false; 		//パラメータチェックフラグ(すべて問題ければ"True")
		final int ARGSCOUNT = 5; 		//argsの数
		final int CODECOUNT = 8; 		//予約コードの文字数
		final String mode = "input";	/*ParcheakクラスでSQL文を作成する際に使用する構成文の差異部分
										(単語"inputplan"と"inputcode"を作成する際に必要)*/
		
		Parcheak PC = new Parcheak();
		
		for (;;) {
			//パラメータの数を確認(１つはサブコマンドで確定、あってなければNG)
			/*if (args.length != ARGSCOUNT) {	
				System.out.println("パラメータの数があっていません");
				break;
			}*/
			
			if (PC.argcheak(args,ARGSCOUNT) ==false) {
				System.out.println("パラメータの数があっていません。(サブコマンド込みで" + ARGSCOUNT +"つ必要です)");
				break;
			}
			
			//パラメータの内容を格納
			int index = 1;
			inputcode =args[index++];
			inputitemid = Integer.parseInt(args[index++]);
			inputitems =Integer.parseInt(args[index++]);
			inputplanday = args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			/*if (inputcode.length() != CODECOUNT) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}*/
			
			if (PC.codecheak(inputcode, CODECOUNT) ==false) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}
			
			//商品コードの内容チェック(商品コードが登録されていない商品や商品コードが0の場合はNG)
			/*if (itemcheak(inputitemid) ==false || inputitemid ==0) {
				System.out.println("商品コードの値が不正です");
				break;
			}*/
			
			if(PC.itemidcheak(inputitemid, conn) == false || inputitemid ==0) {
				System.out.println("商品コードの値が不正です");
				break;
			}
			
			//予定数の内容チェック(予定数は1以上なので、0未満はNG)
			/*if (inputitems < 1) {
				System.out.println("予定数の値が不正です");
				break;
			}*/
			
			if (PC.itemcheak(inputitems) ==false) {
				System.out.println("予定数の値が不正です");
				break;
			}

			//日付の内容チェック(日付は8桁表示なので、8桁ないとNG)
			/*if (inputplanday.length() != 8 ) {
				System.out.println("日付の値が不正です");
				break;
			}*/
			
			if (PC.daycheak(inputplanday) ==false) {
				System.out.println("日付の値が不正です");
				break;
			}
			
			//予約コードの重複確認(inputplanデータ内で重複していたらNG)
			/*if (codecheak(inputcode) == false) {
				System.out.println("予約コードが重複しています");
	    		break;
			}*/
			
			if(PC.dupcodecheak(inputcode, conn, mode) == false) {
				System.out.println("予約コードが重複しています");
	    		break;
			}
			
			parflag = true; //全パラメータが問題なければパラメータフラグをTrueにする
			break;
		}
		return parflag;
	}
	
	//データ記入
	public void datainput() throws SQLException {
		
		final int ITEMID = 1; 
		final int ITEMS = 2;
		final int PLANDAY = 3;
		final int CODE = 4;
		final int STATUS = 5;
		
		//データを記入するSQL文
		String sql2 = "insert into inputplan values( ?, ?, ?, ?, ?) ";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		
	    stmt2.setInt(ITEMID, inputitemid);
	    stmt2.setInt(ITEMS, inputitems);
	    stmt2.setString(PLANDAY, inputplanday);
	    stmt2.setString(CODE, inputcode);
	    stmt2.setInt(STATUS, 0);
	    stmt2.executeUpdate();
	    
        stmt2.close();  
	}
	
	//予約コード重複確認(Parcheakクラスで実装済み)
	/*private boolean codecheak(String code) throws SQLException{
		boolean codeflag = false;
		String sql = "select * from inputplan where inputcode = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1,code);
		ResultSet rs = stmt.executeQuery();
		if (rs.getString("inputcode") == null) {//コードが重複してなかったらnullを返す
			codeflag = true;
		}else {
			codeflag = false;
		}
		rs.close();
		stmt.close();
		return codeflag;
	}*/
	
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
	
	 /*InputList、未実装
	  	public void listinput () throws SQLException {
        String sql3_1 ="into inputlist2 (itemid,inputitems,inputplanday,inputcode,inputstatus) select itemid,inputitems,inputplanday,inputcode,inputstatus from inputplan";
        String sql3_2 ="select max(version) from inputlist2";
        String sql3_3 ="update inputlist2 set version = ? where version is 0";
        
        try {
        	conn.setAutoCommit(false);
        	
        	
        	
        }catch(SQLException e) {
            try {
                // トランザクションのロールバック
                conn.rollback();
                System.out.println("rollback finished");
            } catch (SQLException e2) {
                // スタックトレースを出力
                e2.printStackTrace();
            }
		} finally {
            if(conn != null) {
                try {
                    // オートコミット有効化
                    conn.setAutoCommit(true);
                } catch(SQLException e3) {
                    // スタックトレースを出力
                    e3.printStackTrace();
                }
            }
        }
	}*/
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
