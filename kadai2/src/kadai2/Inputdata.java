package kadai2;
import java.sql.*;

public class Inputdata {
	
	private Connection conn =null;
	private String inputcode; //予約コード
	
	//SQL接続
	public Inputdata(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	//パラメータ確認
	public boolean parcheak(String[] args) throws SQLException {
		boolean parflag = false;
		final int CODECOUNT = 8; //予約コードの文字数
		final int ARGSCOUNT = 2; //argsの数
		final String mode = "input";//ParcheakクラスでSQL文を作成する際に使用する構成文の差異部分(inputplanとinputcodeを作成する)
		Parcheak PC = new Parcheak();
		
		for (;;) {
			if (PC.argcheak(args,ARGSCOUNT) ==false) {
				break;
			}
			
			//パラメータの内容を格納
			int index = 1;
			inputcode =args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			if (PC.codecheak(inputcode, CODECOUNT) ==false) {
				break;
			}
			
			//予約コードの存在確認(inputplanデータ内で存在しなかったら(=重複しなかったら)NG)
			/*if (codecheak(inputcode) == false) {
			System.out.println("予約コードが重複しています");
    		break;
			}*/
			
			if(PC.dupcodecheak(inputcode, conn, mode) == true) {
				System.out.println("予約コードが存在しません");
	    		break;
			}
			
			//入荷状況の確認(指定した予約コードがinputplanデータ内で入荷済み状態ならNG)
			/*if (statuscheak(inputcode) == false) {
				System.out.println("既に入荷済みです");
	    		break;
			}*/
			
			if(PC.statuscheak(inputcode, conn, mode)== false) {
				System.out.println("既に入荷済みです");
	    		break;
			}
			parflag = true;
			break;
		}
		return parflag;
	}
	
	public void datainput() throws SQLException {
		int rackid;
		final int RACKID = 1; 
		final int STATUS = 1;
		final int CODE = 2;
		try {
			conn.setAutoCommit(false); //テーブル自動更新を停止
		
			for (;;) {
				
				//空の棚を検索するSQL
				String sql1 = "select rackid,items from rack where items = 0";
				
				//空の棚を検索
				PreparedStatement stmt1 = conn.prepareStatement(sql1);
				ResultSet rs1 = stmt1.executeQuery();
				rackid = rs1.getInt("rackid");
				rs1.close();
				stmt1.close();
				
				//棚がいっぱいならエラー処理
				if (rackid== 0) {
					System.out.println("棚がいっぱいです。");
					break;
				}
				
				//指定した棚の棚番号に、入荷予定データの予約番号に一致した商品コード、商品数、入荷予定日を入れるSQL
				String sql2 ="update rack set "
						+ "itemid = inputplan.itemid," //棚の商品コードは入荷予定データの商品コード
						+ "items = inputplan.inputitems," //棚の商品数は入荷予定データの入荷予定数
						+ "inputday = inputplan.inputplanday " //棚の入荷日は入荷予定データの入荷予定日
						+ "from inputplan where " //データを入れる条件は
						+ "rack.rackid = ? " //入力位置は棚番号の空き位置であること
						+ "and inputplan.inputcode = ? " //予約番号が同じであること
						+ "and inputplan.inputstatus = 0;"; //商品が未入荷状態であること
				
				//指定した棚の棚番号に、入荷予定データの予約番号に一致した商品コード、商品数、入荷予定日を入れる
				PreparedStatement stmt2 = conn.prepareStatement(sql2);
				stmt2.setInt(RACKID,rackid);
				stmt2.setString(CODE, inputcode);
			    stmt2.executeUpdate();
				stmt2.close();
				
				//入荷後に入荷予定データの入荷済みフラグをオンにするSQL
				String sql3 = "update inputplan set inputstatus = ? where inputcode = ? ";
				
				//入荷後に入荷予定データの入荷済みフラグをオンにする
				PreparedStatement stmt3 = conn.prepareStatement(sql3);
				stmt3.setInt(STATUS, STATUS);
				stmt3.setString(CODE, inputcode);
			    stmt3.executeUpdate();
				stmt3.close();
				
				
				//在庫一覧を更新するSQL
				String sql4 ="replace into stock (itemid,stockitems) "
						+ "select itemid,sum(items) from rack "
						+ "where itemid != 0 group by itemid";
				
				//在庫一覧を更新
				PreparedStatement stmt4 = conn.prepareStatement(sql4);
			    stmt4.executeUpdate();
				stmt4.close();
				
				conn.commit(); //全データ更新
				break;
			}

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
	}
	
	//予約コード存在確認(Parcheakクラスで実装済み)
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
	
	//入荷状況確認(Parcheakクラスで実装済み)
	/*private boolean statuscheak(String code)throws SQLException{
		boolean statusflag = false;
		String sql0 = "select * from inputplan where inputcode = ? and inputstatus = 1";
		
		PreparedStatement stmt0 = conn.prepareStatement(sql0);
		
		stmt0.setString(1,code);
		ResultSet rs0 = stmt0.executeQuery();
		if (rs0.getInt("inputstatus") == 0) {//入荷していなければ0、入荷済みなら1
			statusflag = true;
		}else {
			statusflag = false;
		}
		rs0.close();
		stmt0.close();
		return statusflag;
	}*/
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
