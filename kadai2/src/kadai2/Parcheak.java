/**
 * 
 */
package kadai2;

/**
 パラメータチェックのスーパークラス
 */
 class Parcheak {

	public Parcheak() {
		
	}
	
	//パラメータの数をカウント(１つはサブコマンドで確定)
	public boolean Parcheak(String[] args,int argcount) {
		
		if (args.length != argcount) {	
			System.out.println("パラメータの数があっていません");
			return false;
		}
		return true;
	}
	
	//予約コードの内容チェック(予約コードは8文字なので、予約コードが8文字ないとNG)
	public boolean Parcheak(String inputcode,int codecount) {
		
		if (inputcode.length() != codecount) {
			System.out.println("予約コードが8桁ではありません");
			return false;
		}
		return true;
	}
	
	//予定数の内容チェック(予定数は1以上なので、0未満はNG)
	public boolean Parcheak(int items) {
		
		if (items < 1) {
			System.out.println("予定数の値が不正です");
			return false;
		}
		return true;
	}
	
	//日付の内容チェック(日付は8桁表示なので、8桁ないとNG)
	public boolean Parcheak(String day) {
		if (day.length() != 8 ) {
			System.out.println("日付の値が不正です");
			return false;
		}
		return true;
	}
}
