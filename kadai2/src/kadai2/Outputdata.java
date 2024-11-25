/**
 * 
 */
package kadai2;
import java.sql.*;
/**
 * 
 */
public class Outputdata { //仕掛中
	private Connection conn =null;

	private String outputcode; //予約コード
	
	public Outputdata(String URL) throws SQLException {	
		conn = DriverManager.getConnection(URL);
	}
	
	public boolean parcheak(String[] args) throws SQLException {
		boolean parflag = false;
		final int CODECOUNT = 8; //予約コードの文字数
		final int ARGSCOUNT = 2; //argsの数
		
		for (;;) {
			if (args.length != ARGSCOUNT) {	//パラメータの数をカウント(１つはサブコマンドで確定)
				System.out.println("パラメータの数があっていません");
				break;
			}
			
			//パラメータの内容を格納
			int index = 1;
			outputcode =args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			if (outputcode.length() != CODECOUNT) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}
			
			//予約コードの存在確認(outputplanデータ内で存在しなかったらNG)
			if (codecheak(outputcode) != false) {
				System.out.println("予約コードが存在しません");
	    		break;
			}
			
			//出荷状況の確認(指定した予約コードがoutputplanデータ内で出荷済み状態ならNG)
			if (statuscheak(outputcode) == false) {
				System.out.println("既に出荷済みです");
	    		break;
			}
			parflag = true;
			break;
		}
		return parflag;
	}
	
	public void dataoutput() throws SQLException {
		int rackid;
		final int RACKID = 1; 
		final int STATUS = 1;
		final int CODE = 2;

		//指定商品コードの棚を検索するSQL
		String sql1 = "select rackid,items from rack where itemid = 0";
		
		//指定した棚の棚番号に、出荷予定データの予約番号に一致した商品コード、商品数、出荷予定日を入れるSQL
		String sql2 ="update rack set "
				+ "itemid = outputplan.itemid," //棚の商品コードは出荷予定データの商品コード
				+ "items = outputplan.outputitems," //棚の商品数は出荷予定データの出荷予定数
				+ "outputday = outputplan.outputplanday " //棚の出荷日は出荷予定データの出荷予定日
				+ "from outputplan where " //データを入れる条件は
				+ "rack.rackid = ? " //入力位置は棚番号の空き位置であること
				+ "and outputplan.outputcode = ? " //予約番号が同じ場所であること
				+ "and outputplan.outputstatus = 0;"; //商品が未出荷状態であること
		
		//出荷後に出荷予定データの出荷済みフラグをオンにするSQL
		String sql3 = "update outputplan set outputstatus = ? where outputcode = ? ";
		
		//在庫一覧を更新するSQL
		String sql4 ="replace into stock (itemid,stockitems) "
				+ "select itemid,sum(items) "
				+ "from rack group by itemid";
		
		for (;;) {
			
			//空の棚を検索
			/*PreparedStatement stmt1 = conn.prepareStatement(sql1);
			ResultSet rs1 = stmt1.executeQuery();
			rackid = rs1.getInt("rackid");
			rs1.close();
			stmt1.close();
			
			//棚がいっぱいならエラー処理
			if (rackid== 0) {
				System.out.println("棚がいっぱいです。");
				break;
			}
			*/
			//指定した棚の棚番号に、出荷予定データの予約番号に一致した商品コード、商品数、出荷予定日を入れる
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.setInt(RACKID,rackid);
			stmt2.setString(CODE, outputcode);
		    stmt2.executeUpdate();
			stmt2.close();
			
			//出荷後に出荷予定データの出荷済みフラグをオンにする
			PreparedStatement stmt3 = conn.prepareStatement(sql3);
			stmt3.setInt(STATUS, STATUS);
			stmt3.setString(CODE, outputcode);
		    stmt3.executeUpdate();
			stmt3.close();
			
			//在庫一覧を更新
			PreparedStatement stmt4 = conn.prepareStatement(sql4);
		    stmt4.executeUpdate();
			stmt4.close();
			
			break;
		}
	}
	
	//予約コード存在確認
	private boolean codecheak(String code) throws SQLException{
		boolean codeflag = false;
		String sql = "SELECT * FROM outputplan WHERE outputcode = ?";
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
	}
	
	//出荷状況確認
	private boolean statuscheak(String code)throws SQLException{
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
	}
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
