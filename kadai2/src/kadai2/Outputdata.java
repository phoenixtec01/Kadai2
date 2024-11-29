
package kadai2;
import java.sql.*;

public class Outputdata { 
	
	private Connection conn =null;
	private String outputcode; //予約コード
	
	//SQL接続
	public Outputdata(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	//パラメータ確認(返り値はパラメータチェックフラグ)
	public boolean parcheak(String[] args) throws SQLException {
		
		boolean parflag = false; 		//パラメータチェックフラグ(すべて問題ければ"True")
		final int CODECOUNT = 8; 		//予約コードの文字数
		final int ARGSCOUNT = 2; 		//argsの数
		final String mode = "output";	/*ParcheakクラスでSQL文を作成する際に使用する構成文の差異部分
		 								(単語"outputplan"と"outputcode"と"outputstatus"を作成する際に必要)*/
		
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
			outputcode =args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			/*if (outputcode.length() != CODECOUNT) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}*/
			
			if (PC.codecheak(outputcode, CODECOUNT) ==false) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}
			
			//予約コードの存在確認(outputplanデータ内で存在しなかったらNG)
			/*if (codecheak(outputcode) != false) {
				System.out.println("予約コードが存在しません");
	    		break;
			}*/
			
			if(PC.dupcodecheak(outputcode, conn, mode) == true) {//dupcodecheakメソッドがコード重複時にFalseを返すメソッドのため、Trueを返すと重複しない=存在しない判定になる
				System.out.println("予約コードが存在しません");
	    		break;
			}
			
			//出荷状況の確認(指定した予約コードがoutputplanデータ内で出荷済み状態ならNG)
			/*if (statuscheak(outputcode) == false) {
				System.out.println("既に出荷済みです");
	    		break;
			}*/
			
			if(PC.statuscheak(outputcode, conn, mode)== false) {
				System.out.println("既に出荷済みです");
	    		break;
			}
			parflag = true; //全パラメータが問題なければパラメータフラグをTrueにする
			break;
		}
		return parflag;
	}
	
	//データ記入
	public void datainput() throws SQLException {
		
		int itemid;			//商品コード
		int items; 			//必要出荷数
		int days;			//出荷日
		int itemdata[][]; 	//棚の中のデータ(棚番号と商品数)
		final int STATUS = 1;
		final int CODE = 2;
		
		try {
			conn.setAutoCommit(false); //テーブル自動更新を停止
		
			for (;;) {
				
				//予約コードに記載された商品コードと商品数を取得するSQL文
				String sql0 = "select itemid,outputitems,outputplanday from outputplan where outputcode = ?";
				
				//予約コードに記載された商品コードと商品数を取得する
				PreparedStatement stmt0 = conn.prepareStatement(sql0);
				stmt0.setString(1, outputcode);
				ResultSet rs0 = stmt0.executeQuery();
				itemid = rs0.getInt("itemid");
				items = rs0.getInt("outputitems");
				days = rs0.getInt("outputplanday");
				rs0.close();
				stmt0.close();
				
				//指定商品コードの棚を検索するSQL文
				String sql1 = "select rackid,items from rack where itemid = ?";
				
				//指定した商品IDがある棚を検索
				PreparedStatement stmt1 = conn.prepareStatement(sql1);
				stmt1.setInt(1,itemid);
				ResultSet rs1 = stmt1.executeQuery();
	
				int rowCount = 0;
				while (rs1.next()) {
					rowCount++;
				}
				rs1.close();
				
				//指定した商品IDが棚に無ければエラー処理
				if (rowCount == 0) {
					System.out.println("指定した商品が棚にありません");
					break;
				}
				
				//指定した商品の各棚にある数と在庫数を確認
				itemdata = new int[rowCount][2];
				int i =0;
				int maxitems = 0;		
				ResultSet rs1_1 = stmt1.executeQuery();
				while (rs1_1.next()) {
					itemdata[i][0] = rs1_1.getInt("rackid");
					itemdata[i][1] = rs1_1.getInt("items");
					maxitems = maxitems + rs1_1.getInt("items");
					i++;
				}
				
				rs1_1.close();
				stmt1.close();
				
				//指定した商品の必要数が在庫数以上ならエラー処理
				if (maxitems < items) {
					System.out.println("商品が足りません");
					break;
				}
				
				//指定した棚の棚番号から指定数商品を引き出すSQL文
				String sql2 = "update rack set items = ? where rackid = ?"; 
				
				//出荷が発生した棚番号に出荷日を設定するSQL文
				String sql2_1 = "update rack set outputday = ? where rackid = ?";
				
				int j = 0;
				for	(;;) {
					PreparedStatement stmt2 = conn.prepareStatement(sql2);
					PreparedStatement stmt2_1 = conn.prepareStatement(sql2_1);
					if (itemdata[j][1] < items) {					//指定した棚にある在庫数が現在の必要出荷数を下回る場合は、その棚の中にある全商品を出荷する
						stmt2.setInt(1, 0);							//棚の中の全商品出荷
						stmt2.setInt(2, itemdata[j][0]);			//棚番号指定
						items =items - itemdata[j][1];				//出荷した分、必要出荷数を減らす
						
					}else {											//指定した棚にある在庫数が現在の必要出荷数を上回る場合は、その棚の中から必要出荷数分減らす
						stmt2.setInt(1, itemdata[j][1]-items);		//棚の中から必要出荷数分減らす
						stmt2.setInt(2, itemdata[j][0]);			//棚番号指定
						items = 0; 									//全出荷したので、必要出荷数を0にする
					}
				    stmt2.executeUpdate();
					stmt2.close();
					stmt2_1.setInt(1, days);
					stmt2_1.setInt(2, itemdata[j][0]);
				    stmt2_1.executeUpdate();
				    stmt2_1.close();
				    
					if (items ==0){	//必要分出荷したら終了
						break;
					}
					j++;
				}
				
				
				//出荷後に出荷予定データの出荷済みフラグをオンにするSQL文
				String sql3 = "update outputplan set outputstatus = ? where outputcode = ? ";
	
				//出荷後に出荷予定データの出荷済みフラグをオンにする
				PreparedStatement stmt3 = conn.prepareStatement(sql3);
				stmt3.setInt(STATUS, STATUS);
				stmt3.setString(CODE, outputcode);
			    stmt3.executeUpdate();
				stmt3.close();
				
				
				//在庫一覧を更新するSQL文
				String sql4 = "replace into stock (itemid,stockitems) "
							+ "select itemid,sum(items) from rack "
							+ "where itemid != 0 group by itemid";
				
				//在庫一覧を更新
				PreparedStatement stmt4 = conn.prepareStatement(sql4);
			    stmt4.executeUpdate();
				stmt4.close();
				
				//在庫がなく、入荷日と出荷日が両方入っている棚のデータを表示するSQL文(初期化が必要な棚の検索)
				String sql5 = "select rackid from rack "
							+ "where items = 0 and inputday != '0' and outputday != '0'";
				PreparedStatement stmt5 = conn.prepareStatement(sql5);
				ResultSet rs5 = stmt5.executeQuery();
				
				int rowCount2 = 0;
				while (rs5.next()) {
				    rowCount2++;
				}
				rs5.close();
				
				//初期化する棚が無ければここで全テーブルを更新して終了
				if (rowCount2 == 0) {
					conn.commit(); //全データ更新
					break;
				}
				
				//以下、初期化する棚がある場合の処理
				int resetid[] = new int [rowCount2];
				int k =0;
			
				ResultSet rs5_1 = stmt5.executeQuery();
				while (rs5_1.next()) {
					resetid[k] = rs5_1.getInt("rackid");
					k++;
				}
				rs5_1.close();
				stmt5.close();
				
				//棚データを初期化するSQL文(棚データの商品コードと入荷日と出荷日を0に書き換える)
				String sql6 = "update rack set itemid = 0 , inputday = 0 , outputday = 0 "
							+ "where rackid = ?";
				
				int l =0;
				for(;;) {
					PreparedStatement stmt6 = conn.prepareStatement(sql6);
					stmt6.setInt(1, resetid[l]);
				    stmt6.executeUpdate();
					stmt6.close();
				    l++;
				    
				    if (l == rowCount2) {
				    	break;
				    }			
				}
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
	
	//出荷状況確認(Parcheakクラスで実装済み)
	/*private boolean statuscheak(String code)throws SQLException{
		boolean statusflag = false;
		String sql0 = "select * from outputplan where outputcode = ? and outputstatus = 1";
		
		PreparedStatement stmt0 = conn.prepareStatement(sql0);
		
		stmt0.setString(1,code);
		ResultSet rs0 = stmt0.executeQuery();
		if (rs0.getInt("outputstatus") == 0) {//出荷していなければ0、出荷済みなら1
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
