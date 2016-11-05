package monepla.co.jp.kkb.Model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import monepla.co.jp.kkb.Common.AppModel;
import monepla.co.jp.kkb.Utils.LogFnc;

/**
 * Created by user on 2016/07/12.
 * ユーザ
 */

@Table(name = "user")
public class User extends AppModel {
    @Column(name = COL_USER_NAME)
    public String user_name;
    public static final String COL_USER_NAME = "userName";
    @Column(name = COL_PASSWORD,notNull = false)
    public String password;
    public static final String COL_PASSWORD = "password";
    @Column(name = COL_TEMP_PASSWORD,notNull = false)
    public String temporaryPassword;
    public static final String COL_TEMP_PASSWORD = "temporaryPassword";
    @Column(name = COL_MAIL_ADDRESS,notNull = false)
    public String mailAddress;
    public static final String COL_MAIL_ADDRESS = "mailAddress";
    @Column(name = COL_MAIL_ADDRESS_CONFIRM,notNull = false)
    public String mailAddressConfirm;
    public static final String COL_MAIL_ADDRESS_CONFIRM = "mailAddressConfirm";
    @Column(name = COL_AUTH_DATA,notNull = false)
    public String authData;
    public static final String COL_AUTH_DATA = "authData";
    @Column(name = COL_SESSION_INFO,notNull = false)
    public String sessionInfo;
    public static final String COL_SESSION_INFO = "sessionInfo";
    @Column(name = COL_ACL,notNull = false)
    public String acl;
    public static final String COL_ACL = "acl";
    @Column(name = COL_PUSH_FLG,notNull = false)
    public boolean push_flg;
    public static final String COL_PUSH_FLG = "push_flg";
    private List<Account> accountList = new ArrayList<> ();
    private HashMap<String,List<Cash>> cashListMap = new HashMap<> ();

    public User() {
        super();
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void addAccountList(Account account) {
        accountList.add (account);
    }

    public Account getAccount(String id) {
        for (Account account : accountList) {
            if (account.objectId == null) LogFnc.Logging (LogFnc.ERROR,"null",LogFnc.current ());
            if (account.objectId.equals (id)) {
                return account;
            }
        }
        return null;
    }

    public HashMap<String, List<Cash>> getCashListMap() {
        return cashListMap;
    }

    public void setCashListMap(HashMap<String, List<Cash>> cashListMap) {
        this.cashListMap = cashListMap;
    }
}
