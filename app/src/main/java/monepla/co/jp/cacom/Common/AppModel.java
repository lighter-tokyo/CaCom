package monepla.co.jp.cacom.Common;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created on 2016/07/12.
 * 共通model
 * @see Model
 * @see Serializable
 */
abstract public class AppModel extends Model implements Serializable {

    /** objectId */
    @Column(name = COL_OBJECT_ID,notNull = false,unique = true)
    public String objectId;
    public static final String COL_OBJECT_ID = "objectId";
    /** 作成日 */
    @Column(name = COL_CREATED_DATE)
    public Date createDate;
    public static final String COL_CREATED_DATE = "createdDate";
    /** 更新日 */
    @Column(name = COL_UPDATED_DATE)

    public Date updateDate;
    public static final String COL_UPDATED_DATE = "updatedDate";
    /** 作成者 */
    @Column(name = COL_CREATED_USER,notNull = false)
    public String createdUser;
    public static final String COL_CREATED_USER = "createdUser";
    /** 更新者 */
    @Column(name = COL_UPDATED_USER,notNull = false)
    public String updatedUser;
    public static final String COL_UPDATED_USER = "updatedUser";
    /** 削除フラグ */
    @Column(name = COL_DEL_FLG,notNull = true)
    public boolean delFlg;
    public static final String COL_DEL_FLG = "delFlg";

    /** エラー情報 */
    private NCMBException err;

    public AppModel() {
        super();
    }

    /**
     * 同一Id可否
     * @param id objectId
     * @return 可否
     */
    public boolean equalId(String id) {
        return this.objectId.equals (id);
    }

    /**
     * err設定
     * @param msg
     */
    public void setErr(NCMBException msg) {
        err = msg;
    }

    /**
     * エラー取得
     * @return エラー
     */
    public NCMBException getErr() {
        return err;
    }

    public void getToAppModel(Class<? extends AppModel> table , NCMBObject object) {

        try {
            for (Field field : table.getDeclaredFields ()) {
                Object value = null;
                Class<?> fieldType = field.getType();
                if(field.getAnnotation(Column.class) != null){
                    Column element = field.getAnnotation (Column.class);

                    field.setAccessible(true);
                    if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                        value = object.getInt(element.name ());
                    }
                    else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                        value = object.getInt(element.name ());
                    }
                    else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                        value = object.getInt(element.name ());
                    }
                    else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                        value = object.getLong(element.name ());
                    }
                    else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                        value = object.getDouble(element.name ());
                    }
                    else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                        value = object.getInt(element.name ()) != 0;
                    }
                    else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                        value = object.getString(element.name ()).charAt(0);
                    }
                    else if (fieldType.equals(String.class)) {
                        value = object.getString (element.name ());
                    }
                    else if (fieldType.equals (Date.class)) {
                        value = object.getDate (element.name ());
                    }
                }
                // Set the field value
                if (value != null) {
                    field.set(this, value);
                }
            }
            objectId = object.getObjectId ();
            createDate = object.getCreateDate ();
            updateDate = object.getUpdateDate ();
            createdUser = object.getString (COL_CREATED_USER);
            updatedUser = object.getString (COL_UPDATED_USER);
            delFlg = object.getBoolean (COL_DEL_FLG);
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        }
        save ();

    }

}
