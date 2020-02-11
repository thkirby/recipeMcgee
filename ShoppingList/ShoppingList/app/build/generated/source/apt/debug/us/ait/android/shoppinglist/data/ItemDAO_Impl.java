package us.ait.android.shoppinglist.data;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ItemDAO_Impl implements ItemDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ItemDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItem = new EntityInsertionAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Item`(`itemID`,`itemName`,`description`,`estimatedPrice`,`itemType`,`bought`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
        if (value.getItemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getItemName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        stmt.bindDouble(4, value.getEstimatedPrice());
        stmt.bindLong(5, value.getItemType());
        final int _tmp;
        _tmp = value.isBought() ? 1 : 0;
        stmt.bindLong(6, _tmp);
      }
    };
    this.__deletionAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Item` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Item` SET `itemID` = ?,`itemName` = ?,`description` = ?,`estimatedPrice` = ?,`itemType` = ?,`bought` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
        if (value.getItemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getItemName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        stmt.bindDouble(4, value.getEstimatedPrice());
        stmt.bindLong(5, value.getItemType());
        final int _tmp;
        _tmp = value.isBought() ? 1 : 0;
        stmt.bindLong(6, _tmp);
        stmt.bindLong(7, value.getItemID());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM item";
        return _query;
      }
    };
  }

  @Override
  public long insertItem(Item item) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfItem.insertAndReturnId(item);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Item item) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Item item) {
    __db.beginTransaction();
    try {
      __updateAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<Item> getAll() {
    final String _sql = "SELECT * FROM item";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfItemID = _cursor.getColumnIndexOrThrow("itemID");
      final int _cursorIndexOfItemName = _cursor.getColumnIndexOrThrow("itemName");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfEstimatedPrice = _cursor.getColumnIndexOrThrow("estimatedPrice");
      final int _cursorIndexOfItemType = _cursor.getColumnIndexOrThrow("itemType");
      final int _cursorIndexOfBought = _cursor.getColumnIndexOrThrow("bought");
      final List<Item> _result = new ArrayList<Item>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Item _item;
        final String _tmpItemName;
        _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final double _tmpEstimatedPrice;
        _tmpEstimatedPrice = _cursor.getDouble(_cursorIndexOfEstimatedPrice);
        final int _tmpItemType;
        _tmpItemType = _cursor.getInt(_cursorIndexOfItemType);
        final boolean _tmpBought;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfBought);
        _tmpBought = _tmp != 0;
        _item = new Item(_tmpItemName,_tmpDescription,_tmpEstimatedPrice,_tmpItemType,_tmpBought);
        final long _tmpItemID;
        _tmpItemID = _cursor.getLong(_cursorIndexOfItemID);
        _item.setItemID(_tmpItemID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
