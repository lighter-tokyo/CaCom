package monepla.co.jp.cacom.Interface;

import monepla.co.jp.cacom.Common.BaseFragment;

/**
 * Activityへの通知
 * Created on 2016/07/30.
 */
public interface ActivityListener {
    /**
     * タイトル設定
     * @param title タイトル
     */
    void setToolbarTitle(int title);

    /**
     * プログレス表示
     * @param title タイトル
     */
    void showProgress(int title);

    /**
     * プログレス非表示
     */
    void closeProgress();

    /**
     * ドロワー表示
     */
    void showDrawer();

    void closeDrawer();

    void addStackFragment(BaseFragment fragment);

    void logout();
}
