package com.example.week3

import android.content.ContentValues
import android.content.Context
import android.widget.Toast
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.week3.modal.*
import java.io.File
import java.io.FileOutputStream




class DPHelper(context: Context) {

    private var DATABASE_NAME = "db_week_3.db"
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
            if (!dbFile.exists()) try {
                CopyDatabaseFromAsset()
                Toast.makeText(context, "Copy successful", Toast.LENGTH_SHORT).show()

            } catch (ex: Exception) {
                ex.printStackTrace()
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

    fun getAllData() : ArrayList<Data>{
        val arr = ArrayList<Data>()
        val sql = "Select * from GraphObject , Fromm , DataAttachments where fromm = id_from and GraphObject.id = id_attach"
        val cursor = db?.rawQuery(sql , null)
        if (cursor != null) {
            Log.d("AAA" , "SizeCursor : ${cursor.count}")
            while (cursor.moveToNext()){
                val arrAttach = ArrayList<DataXX>()
                arrAttach.add(
                    DataXX(
                        cursor.getString(cursor.getColumnIndex("description")),
                        null,
                        null,
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("url"))
                    ))
                arr.add(
                    Data(
                        cursor.getString(cursor.getColumnIndex("createdTime")).toLong(),
                        cursor.getString(0),
                        cursor.getString(cursor.getColumnIndex("message")),
                        From(cursor.getString(cursor.getColumnIndex("name")),
                            null ,
                            cursor.getString(cursor.getColumnIndex("id"))),
                        Likes(null, null, null),
                        Attachments(arrAttach),
                        cursor.getString(cursor.getColumnIndex("objectId"))
                    )
                )
                Log.d("AAA" , "SizeArr : ${arr.size}")

            }
        }
        cursor?.close()
        return arr
    }

    fun insertData(arr : ArrayList<Data>){
        for(datum in arr){
            val sqlCheckFrom = "Select * from Fromm where id_from = ${datum.from?.id}"

            val cursor = db?.rawQuery(sqlCheckFrom , null)
            if(cursor != null)
                if(cursor.count <= 0)
                {
                    cursor.close()
                    val k = insertFrom(datum.from)
                    if(k != null && k <= 0){
                       Toast.makeText(context , "There is a problem" , Toast.LENGTH_SHORT).show()
                        return
                    }
                }

            // Insert GraphObject
            val contentValues = ContentValues()
            contentValues.put("id", datum.id.toString())
            contentValues.put("created_time", datum.createdTime.toString())
            contentValues.put("message", datum.message.toString())
            contentValues.put("object_id", datum.objectId.toString())
            contentValues.put("fromm", datum.from?.id.toString())

            val kqq = db?.insert("Datum", null, contentValues)

            if(kqq != null && kqq <= 0){
                Toast.makeText(context , "There is a problem" , Toast.LENGTH_SHORT).show()
            }
            else{
                val kq = insertAttachments(datum.attachments!!.data!![0], datum.id)
                if(kq != null && kq <= 0){
                    Toast.makeText(context , "There is a problem" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insertAttachments(dataAttachments : DataXX? , id : String?) : Long?{
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