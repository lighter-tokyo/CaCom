package monepla.co.jp.kkb.Utils;

import com.activeandroid.query.Select;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;

import java.util.List;

import monepla.co.jp.kkb.Model.Account;
import monepla.co.jp.kkb.Model.Cash;
import monepla.co.jp.kkb.Model.Category;

/**
 * Created by user on 2016/09/26.
 */
public class Queue implements FindCallback<NCMBObject>{
    private static Queue ourInstance = new Queue ();

    private onObserver onObserver;
    public interface onObserver {
        void done(String tableName);
    }

    public static Queue getInstance() {
        return ourInstance;
    }

    public void setOnObserver(Queue.onObserver onObserver) {
        this.onObserver = onObserver;
    }


    private Queue() {}

    synchronized public void put(NCMBQuery<NCMBObject> object) {
            object.findInBackground (this);
    }



    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        if (e != null) {
            LogFnc.Logging (LogFnc.ERROR,e.getMessage (),LogFnc.current ());
            return;
        }
        setLocal (list);
    }

    private void setLocal (List<NCMBObject> list) {
        String tableName = "";
        for (NCMBObject object : list) {
            if (object.getClassName ().equals (Category.TABLE_NAME)) {
                Category category = new Select ().from (Category.class).where (Category.COL_OBJECT_ID + " = ? ",object.getObjectId () ).executeSingle ();
                if (category == null) {
                    /** インサート */
                    category = new Category ();
                }
                category.getToAppModel (Category.class,object);
                tableName = Category.TABLE_NAME;
            } else if (object.getClassName ().equals (Account.TABLE_NAME)) {
                Account account = new Select ().from (Account.class).where (Account.COL_OBJECT_ID + " = ? ",object.getObjectId () ).executeSingle ();
                if (account == null) {
                    /** インサート */
                    account = new Account ();
                }
                account.getToAppModel (Account.class,object);
                tableName = Account.TABLE_NAME;

            } else if (object.getClassName ().equals (Cash.TABLE_NAME)) {
                Cash cash = new Select ().from (Cash.class).where (Cash.COL_OBJECT_ID + " = ? ",object.getObjectId () ).executeSingle ();
                if (cash == null) {
                    /** インサート */
                    cash = new Cash ();
                }
                cash.getToAppModel (Cash.class,object);
                tableName = Cash.TABLE_NAME;
            }
        }
        onObserver.done (tableName);
    }



}
