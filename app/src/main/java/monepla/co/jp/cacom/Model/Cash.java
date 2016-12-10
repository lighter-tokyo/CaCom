package monepla.co.jp.cacom.Model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

import monepla.co.jp.cacom.Common.AppModel;

/**
 * 入出金モデル
 * Created on 2016/07/10.
 * @see monepla.co.jp.cacom.Common.AppModel
 */
@Table(name = "Cash")
public class Cash extends AppModel {
    public static final String TABLE_NAME = Cash.class.getSimpleName();


    public static final String COL_USER_ID = "userId";
    /**
     * ユーザid
     */
    @Column(name = COL_USER_ID)
    public String user_id;

    public static final String COL_CASH_DATE = "cashDate";
    /**
     * `入出金日付
     */
    @Column(name = COL_CASH_DATE)
    public Date cashDate;

    public static final String COL_CATEGORY_ID = "categoryId";
    /**
     * カテゴリーid
     */
    @Column(name = COL_CATEGORY_ID)
    public String category_id;

    public static final String COL_ORIGIN_ACCOUNT = "originAccountId";
    /**
     * 元引き出し
     */
    @Column(name = COL_ORIGIN_ACCOUNT)
    public String originAccountId;

    public static final String COL_CASH_DIV = "cashDiv";
    /**
     * 入出金フラグ(入金；０、出金；１、振替：２)
     */
    @Column(name = COL_CASH_DIV)
    public Integer cashDiv;

    public static final String COL_DETAIL = "detail";
    /**
     * 詳細名称
     */
    @Column(name = "detail")
    public String detail;

    public static final String COL_AMOUNT = "amount";
    /**
     * 金額
     */
    @Column(name = "amount")
    public Long amount;

    public Cash(){
        super();
    }

}
