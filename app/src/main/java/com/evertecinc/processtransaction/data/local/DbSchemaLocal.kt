package com.evertecinc.processtransaction.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.evertecinc.processtransaction.R
import com.evertecinc.processtransaction.data.local.dao.ProductDao
import com.evertecinc.processtransaction.data.local.dao.PurchaseDetailDao
import com.evertecinc.processtransaction.data.local.dao.TokenCreditCardDao
import com.evertecinc.processtransaction.data.local.dao.TransactionDao
import com.evertecinc.processtransaction.data.local.entity.ProductEntity
import com.evertecinc.processtransaction.data.local.entity.PurchaseDetailEntity
import com.evertecinc.processtransaction.data.local.entity.TokenCreditCardEntity
import com.evertecinc.processtransaction.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class,
        TokenCreditCardEntity::class,
        ProductEntity::class,
        PurchaseDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DbSchemaLocal : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun tokenCreditCardDao(): TokenCreditCardDao
    abstract fun transactionDao(): TransactionDao
    abstract fun purchaseDetailDao(): PurchaseDetailDao

    companion object {
        @Volatile
        private var INSTANCE: DbSchemaLocal? = null

        fun getInstance(context: Context): DbSchemaLocal {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DbSchemaLocal::class.java,
                    context.resources.getString(R.string.database_name)
                )
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private val sRoomDatabaseCallback: Callback =
            object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            }
    }
}