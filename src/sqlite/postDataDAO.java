package sqlite;

import java.util.ArrayList;
import java.util.List;

import ranking.RankData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class postDataDAO {
	private MyDBHelper dbhelper;
	// 表格名稱
	public static final String TABLE_NAME = "localPostDatas";

	// 編號表格欄位名稱，固定不變
	public static final String KEY_ID = "_id";

	// 其它表格欄位名稱
	public static final String NAME_COLUMN = "name";
	public static final String SCORE_COLUMN = "score";
	public static final String DATETIME_COLUMN = "date";

	// 使用上面宣告的變數建立表格的SQL指令
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NAME_COLUMN + " TEXT NOT NULL, " + DATETIME_COLUMN
			+ " TEXT NOT NULL, " + SCORE_COLUMN + " TEXT NOT NULL )";

	// 資料庫物件
	private SQLiteDatabase db;

	// 建構子，一般的應用都不需要修改
	public postDataDAO(Context context) {
		dbhelper = new MyDBHelper(context, null, null, 0);
		db = dbhelper.getDatabase(context);
	}

	// 關閉資料庫，一般的應用都不需要修改
	public void close() {
		dbhelper.close();
		db.close();
	}

	// 新增參數指定的物件
	public RankData insert(RankData item) {
		// 建立準備新增資料的ContentValues物件
		ContentValues cv = new ContentValues();

		// 加入ContentValues物件包裝的新增資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		cv.put(NAME_COLUMN, item.getName());
		cv.put(SCORE_COLUMN, item.getScore());
		cv.put(DATETIME_COLUMN, item.getDate());

		// 新增一筆資料並取得編號
		// 第一個參數是表格名稱
		// 第二個參數是沒有指定欄位值的預設值
		// 第三個參數是包裝新增資料的ContentValues物件
		long id = db.insert(TABLE_NAME, null, cv);

		// 設定編號
		item.id = id;
		// 回傳結果
		return item;
	}

	// 修改參數指定的物件
	public boolean update(RankData item) {
		// 建立準備修改資料的ContentValues物件
		ContentValues cv = new ContentValues();

		// 加入ContentValues物件包裝的修改資料
		// 第一個參數是欄位名稱， 第二個參數是欄位的資料
		cv.put(NAME_COLUMN, item.getName());
		cv.put(SCORE_COLUMN, item.getScore());
		cv.put(DATETIME_COLUMN, item.getDate());

		// 設定修改資料的條件為編號
		// 格式為「欄位名稱＝資料」
		String where = KEY_ID + "=" + item.id;

		// 執行修改資料並回傳修改的資料數量是否成功
		return db.update(TABLE_NAME, cv, where, null) > 0;
	}

	// 刪除參數指定編號的資料
	public boolean delete(long id) {
		// 設定條件為編號，格式為「欄位名稱=資料」
		String where = KEY_ID + "=" + id;
		// 刪除指定編號資料並回傳刪除是否成功
		return db.delete(TABLE_NAME, where, null) > 0;
	}

	// 讀取所有記事資料
	public List<RankData> getAll() {
		List<RankData> result = new ArrayList<RankData>();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				"_id DESC", null);

		while (cursor.moveToNext()) {
			result.add(getRecord(cursor));
		}

		cursor.close();
		return result;
	}

	// 取得指定編號的資料物件
	public RankData get(long id) {
		// 準備回傳結果用的物件
		RankData item = null;
		// 使用編號為查詢條件
		String where = KEY_ID + "=" + id;
		// 執行查詢
		Cursor result = db.query(TABLE_NAME, null, where, null, null, null,
				null, null);

		// 如果有查詢結果
		if (result.moveToFirst()) {
			// 讀取包裝一筆資料的物件
			item = getRecord(result);
		}

		// 關閉Cursor物件
		result.close();
		// 回傳結果
		return item;
	}

	// 把Cursor目前的資料包裝為物件
	public RankData getRecord(Cursor cursor) {
		// 準備回傳結果用的物件
		long id = cursor.getLong(0);
		String name = cursor.getString(1);
		String date = cursor.getString(2);
		String score = cursor.getString(3);
		RankData result = new RankData(id, score, date, name);

		// 回傳結果
		return result;
	}

	// 取得資料數量
	public int getCount() {
		int result = 0;
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}
}
