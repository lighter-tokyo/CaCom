package monepla.co.jp.kkb.Model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import monepla.co.jp.kkb.Common.AppModel;

/**
 * 口座モデル
 * Created on 2016/08/03.
 * @see AppModel
 */
@Table (name = "Account")
public class Account extends AppModel {
    public static final String TABLE_NAME = Account.class.getSimpleName ();
    public Account() {
        super();
    }

    public static final String COL_USER_ID = "userId";
    /** ユーザId */
    @Column(name = COL_USER_ID)
    public String userId;

    public static final String COL_ACCOUNT_NAME = "accountName";
    /** 口座名 */
    @Column(name = COL_ACCOUNT_NAME)
    public String accountName;

    public static final String COL_ACCOUNT_DIV = "accountDiv";
    @Column(name = COL_ACCOUNT_DIV)
    public int accountDiv;



}
