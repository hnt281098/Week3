package com.example.week3

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.week3.data.*
import java.io.File
import java.io.FileOutputStream




class DPHelper(context: Context) {

    private var DATABASE_NAME = "db.db"
    private val DB_PATH_SUFFIX = "/databases/"
    private var db: SQLiteDatabase? = null

    var context: Context? = null

    init {
        this.context = context
        processSQLite()
    }

    private fun processSQLite() {
        val dbFile = context?.getDatabasePath(DATABASE_NAME)
        if (dbFile != null) {
            if (!dbFile.exists()) {
                try {
                    CopyDatabaseFromAsset()
                    Toast.makeText(context, "Copy successful !!!", Toast.LENGTH_SHORT).show()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
        }
    }

    private fun CopyDatabaseFromAsset() {
        try {
            val databaseInputStream = context?.assets?.open("sql/$DATABASE_NAME")


            val outputStream = getPathDatabaseSystem()

            val file = File(context?.applicationInfo?.dataDir + DB_PATH_SUFFIX)
            if (!file.exists()) {
                file.mkdir()
            }

            val databaseOutputStream = FileOutputStream(outputStream)

            val buffer = ByteArray(1024)
            var length = 0
            while (true) {
                if (databaseInputStream != null) length = databaseInputStream.read(buffer)
                if(length > 0)
                    databaseOutputStream.write(buffer, 0, length)
                else
                    break
            }

            databaseOutputStream.flush()
            databaseOutputStream.close()
            databaseInputStream?.close()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun getPathDatabaseSystem(): String {
        return context?.applicationInfo?.dataDir + DB_PATH_SUFFIX + DATABASE_NAME
    }

    fun initDB(){
        db = context?.openOrCreateDatabase(DATABASE_NAME , Context.MODE_PRIVATE , null)
    }

    fun getAllData() : ArrayList<Datum>{
        val arr = ArrayList<Datum>()
        val sql = "Select * from Datum , Fromm , DataAttachments where fromm = id_from and Datum.id = id_attach"
        val cursor = db?.rawQuery(sql , null)
        if (cursor != null) {
            Log.d("AAABBB" , "SizeCursor : ${cursor.count}")
            while (cursor.moveToNext()){
                val arrAtach = ArrayList<DataAttachments>()
                arrAtach.add(
                    DataAttachments(cursor.getString(cursor.getColumnIndex("description")),
                        null,
                        null,
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("url"))
                    ))
                arr.add(
                    Datum(
                        cursor.getString(cursor.getColumnIndex("created_time")).toLong(),
                        cursor.getString(0),
                        cursor.getString(cursor.getColumnIndex("message")),
                        cursor.getString(cursor.getColumnIndex("object_id")),
                        Likes(null),
                        From(cursor.getString(cursor.getColumnIndex("name")),
                            null ,
                            cursor.getString(cursor.getColumnIndex("id_from"))),

                        Attachments(arrAtach)
                    )
                )
                Log.d("AAABBB" , "SizeArr : ${arr.size}")

            }
        }
        cursor?.close()
        return arr
    }

    fun insertData(arr : ArrayList<Datum>){
        for(datum in arr){
            val sqlCheckFrom = "Select * from Fromm where id_from = ${datum.from?.id}"

            val cursor = db?.rawQuery(sqlCheckFrom , null)
            if(cursor != null)
                if(cursor.count <= 0)
                {
                    cursor.close()
                    val k = insertFrom(datum.from)
                    if(k != null && k <= 0){
                       Toast.makeText(context , "Có sự cố xảy ra" , Toast.LENGTH_SHORT).show()
                        return
                    }
                }

            // Insert Datum
            val contentValues = ContentValues()
            contentValues.put("id", datum.id)
            contentValues.put("created_time", datum.created_time.toString())
            contentValues.put("message", datum.message)
            contentValues.put("object_id", datum.object_id)
            contentValues.put("fromm", datum.from?.id)

            val kqq = db?.insert("Datum", null, contentValues)

            if(kqq != null && kqq <= 0){
                Toast.makeText(context , "Có sự cố xảyy raaa" , Toast.LENGTH_SHORT).show()
            }
            else{
                val kq = insertAttachments(datum.attachments?.data?.get(0) , datum.id)
                if(kq != null && kq <= 0){
                    Toast.makeText(context , "Có sự cố xảyy ra" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insertAttachments(dataAttachments : DataAttachments? , id : String?) : Long?{
        val contentValues = ContentValues()
        contentValues.put("id_attach", id)
        contentValues.put("title", dataAttachments?.title)
        contentValues.put("type", dataAttachments?.type)
        contentValues.put("url", dataAttachments?.url)
        contentValues.put("description", dataAttachments?.description)

        return db?.insert("DataAttachments", null, contentValues)
    }

    private fun insertFrom(frm : From?) : Long?{
        val contentValues = ContentValues()
        contentValues.put("id_from", frm?.id)
        contentValues.put("name", frm?.name)

        return db?.insert("Fromm", null, contentValues)
    }

    fun clearData(){
        db?.delete("DataAttachments" , null , null)
        db?.delete("Datum" , null , null)
        db?.delete("Fromm" , null , null)
    }
}