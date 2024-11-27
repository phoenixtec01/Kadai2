package kadai2;
import java.sql.*;

public class Inputplan extends Parcheak{

	//private final String SQL = "select * from employee where id =? ;";
	private Connection conn =null;
	private int inputitemid; //商品コード
	private int inputitems; //予定数
	private String inputplanday; //予定日
	private String inputcode; //予約コード
	
	//SQL接続
	public Inputplan(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
		
	}
	
	//パラメータ確認
	public boolean Parcheak(String[] args) throws SQLException {
		boolean parflag = false;
		final int CODECOUNT = 8; //予約コードの文字数
		final int ARGSCOUNT = 5; //argsの数
		
		for (;;) {
			//パラメータの数をカウント(１つはサブコマンドで確定)
			/*if (args.length != ARGSCOUNT) {	
				System.out.println("パラメータの数があっていません");
				break;
			}*/
			
			boolean argflag =super.Parcheak(args,ARGSCOUNT);
			if (argflag ==false) {
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
			
			boolean codeflag = super.Parcheak(inputcode, CODECOUNT);
			if (codeflag ==false) {
				break;
			}
			//商品コードの内容チェック(商品コードが登録されていない商品や商品コードが0の場合はNG)
			if (itemcheak(inputitemid) ==false || inputitemid ==0) {
				System.out.println("商品コードの値が不正です");
				break;
			}
			
			//予定数の内容チェック(予定数は1以上なので、0未満はNG)
			/*if (inputitems < 1) {
				System.out.println("予定数の値が不正です");
				break;
			}*/
			
			boolean itemflag = super.Parcheak(inputitems);
			if (itemflag ==false) {
				break;
			}

			//日付の内容チェック(日付は8桁表示なので、8桁ないとNG)
			/*if (inputplanday.length() != 8 ) {
				System.out.println("日付の値が不正です");
				break;
			}*/
			
			boolean dayflag = super.Parcheak(inputplanday);
			if (dayflag ==false) {
				break;
			}
			
			//予約コードの重複確認(inputplanデータ内で重複したらNG)
			if (codecheak(inputcode) == false) {
				System.out.println("予約コードが重複しています");
	    		break;
			}
			
			parflag = true;
			break;
		}
		return parflag;
	}
	

	//予約コード重複確認
	private boolean codecheak(String code) throws SQLException{
		boolean codeflag = false;
		String sql = "SELECT * FROM inputplan WHERE inputcode = ?";
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
	}
	
	//商品コードチェック
	private boolean itemcheak(int id) throws SQLException{
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
	}
	
	//データ記入
	public void datainput() throws SQLException {
		final int ITEMID = 1; 
		final int ITEMS = 2;
		final int PLANDAY = 3;
		final int CODE = 4;
		final int STATUS = 5;
		String sql2 = "INSERT INTO inputplan VALUES( ?, ?, ?, ?, ?) ";
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		
	    stmt2.setInt(ITEMID, inputitemid);
	    stmt2.setInt(ITEMS, inputitems);
	    stmt2.setString(PLANDAY, inputplanday);
	    stmt2.setString(CODE, inputcode);
	    stmt2.setInt(STATUS, 0);
	    stmt2.executeUpdate();
	    
        stmt2.close();
        
       
	}
	
	/* public void listinput () throws SQLException {
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
