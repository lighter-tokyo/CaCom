package monepla.co.jp.kkb.Constract;

import monepla.co.jp.kkb.R;

/**
 * Created by user on 2016/07/11.
 */
public class CommonConst {
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String RIGHT_JOIN = " RIGHT JOIN ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String ASC = " ASC ";
    public static final String DESC = " DESC ";

    public static final String PREF_UPDATE = "update";
    public static final String PREF_CATEGORY_UPDATE = "category_update";
    public static final String BLANK = "";
    public static final String PREF_PASS_CODE = "pas_code";


    public enum OtherItems {
//        ABOUT(R.string.other_about),
//        FAQ(R.string.other_fag),
        PASS_CODE(R.string.other_pass_code),
        PUSH(R.string.other_push),
//        TERM(R.string.other_term),
        VERSION(R.string.other_version),
//        QUIT(R.string.other_quit),
        LOGOUT(R.string.other_logout);


        private int string_id;
        OtherItems(int id) {
            string_id = id;
        }
        public int getItems() {
            return string_id;
        }
        public void setItems(int id) {
            string_id = id;
        }
    }

    public enum CategoryDiv {
        OUTPUT(R.id.plus_output,R.string.output),
        INPUT (R.id.plus_input,R.string.input),
        CHANGE(R.id.plus_move,R.string.move);

        int id;
        int name;
        CategoryDiv(int id,int name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public int getName() {
            return name;
        }
    }

    public enum AccountDiv {
        OTHER(0,R.string.account_other),
        BANK(1,R.string.account_bank),
        TRUST(2,R.string.account_trust),
        SECURITIES(3,R.string.account_securities),
        CREDIT(4,R.string.account_credit);

        int id;
        int str;
        AccountDiv(int id,int str) {
            this.id = id;
            this.str = str;
        }
        public int getId() {
            return id;
        }
        public int getStr() {
            return str;
        }
    }
}
