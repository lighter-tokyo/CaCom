package monepla.co.jp.cacom.Model;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import monepla.co.jp.cacom.Common.AppModel;


/**
 * カテゴリーモデル
 * Created on 2016/07/11.
 * @see monepla.co.jp.cacom.Common.AppModel
 */
@Table(name = "Category")
public class Category extends AppModel {
    public Category() {
        super();
    }
    public static final String TABLE_NAME = Category.class.getSimpleName();

    public static final String COL_CATEGORY_NAME =  "CategoryName";
    /**
     * カテゴリー名称
     */
    @Column(name = COL_CATEGORY_NAME)
    public String category_name;

    public static final String COL_CASH_DIV = "cashDiv";
    /**
     * 入出金フラグ(入金；０、出金；１、振替：２)
     */
    @Column(name = COL_CASH_DIV)
    public Integer cashDiv;

    public static final String COL_USER_ID = "userId";
    /**
     *
     */
    @Column(name = COL_USER_ID)
    public String userId;

}
