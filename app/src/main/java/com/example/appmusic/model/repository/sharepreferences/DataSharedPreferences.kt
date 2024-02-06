package com.example.appmusic.model.repository.sharepreferences

import android.content.Context
import android.content.SharedPreferences

class DataSharedPreferences(private var fileSharedPreferences: String, var context: Context) {

    fun putStateMediaPlayer(key:String, value: Boolean) { //put dữ liệu vào
        var sharedPreferences: SharedPreferences = context.getSharedPreferences( // tạo ra đối tượng để lưu trữ dữ liệu
            fileSharedPreferences, // tên file bất kì. dữ liệu được lưu trong tên_file.xml ở đường dẫn DATA/data/app_name/share_prefs/tên_file.xml
            Context.MODE_PRIVATE // chế độ đọc ghi dữ liệu
        )
        var editor: SharedPreferences.Editor = sharedPreferences.edit() // để chỉnh sửa dữ liệu thì cần tạo đối tượng để chỉnh sửa dữ liệu từ đối tượng lưu dữ liệu
        editor.putBoolean(key,value) // lưu dữ liệu ở các dạng cho phép
        editor.apply() // dùng để xác nhận lưu trữ dữ liệu nhưng không quan
//        editor.commit() // dùng để xác nhận lưu trữ dữ liệu và trả về kết quả xác nhận dữ liệu đã được lưu hay chưa
//        editor.clear() // xóa hết toàn bộ dữ liệu
//        editor.remove(key) //xóa dữ liệu ứng với key
    }
    fun getStateMediaPlayer(key:String): Boolean {  // lấy dữ liệu ra
        var sharedPreferences: SharedPreferences = context.getSharedPreferences( // tương tự như trên
            fileSharedPreferences,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(key,false) // lấy ra dữ liệu đã được lưu từ key đã được đánh dấu
    }
}