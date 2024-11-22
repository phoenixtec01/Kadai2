package kadai2;
import java.sql.*;

public class Inputdata {
	private Connection conn =null;
	private int inputitemid; //商品コード
	private int inputitems; //予定数
	private int inputplanday; //予定日
	private String inputcode; //予約コード
	
	public Inputdata(String URL) throws SQLException {	
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
			inputcode =args[index++];
						
			//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
			if (inputcode.length() != CODECOUNT) {
				System.out.println("予約コードが8桁ではありません");
				break;
			}
			
			parflag = true;
			break;
		}
		return parflag;
	}
	
	public void datainput() throws SQLException {
		//棚の数をカウント
		//String sql1 = "select count(*) from rack";
		
		String sql2 = "select rackid,items from rack where items = 0";
		
		//指定した棚の棚番号に、入荷予定データの予約番号に一致した商品コード、商品数、入荷予定日を入れる
		String sql3 ="update rack set itemid = inputplan.itemid,"
				+ "items = inputplan.inputitems,"
				+ "inputday = inputplan.inputplanday "
				+ "from inputplan where rack.rackid = ? "
				+ "and inputplan.inputcode = ?";
		
		//入荷後に入荷予定データの入荷済みフラグをオンにする
		String sql4 = "update inputplan set inputstatus = 1 where inputcode = ? ";
		

		
		/*PreparedStatement stmt2 = conn.prepareStatement(sql2);

		stmt2.close();
		
		PreparedStatement stmt3 = conn.prepareStatement(sql3);
		stmt3.close();
		
		PreparedStatement stmt4 = conn.prepareStatement(sql4);
		stmt4.close();*/
	}
	
	//SQL切断
	public void sqlclose() throws SQLException {
		if (conn !=null) {
			conn.close();
		}
	}
}
