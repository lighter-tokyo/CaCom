package monepla.co.jp.kkb.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import monepla.co.jp.kkb.R;

/**
 * Created by user on 2016/07/14.
 * その他リストビュー
 */
public class OtherController extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<Integer> items;
    private OnOtherClickListener onOtherClickListener;
    public interface OnOtherClickListener {
        void onOtherClick(int item);
    }
    /** Constructor **/
    public OtherController(OnOtherClickListener listener, List<Integer> items) {
        this.items = items;
        onOtherClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relative_other_row, parent, false);

        return new OtherHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int item = items.get(position);
        OtherHolder oh = (OtherHolder) holder;
        oh.textView.setText (item);
        oh.textView.setOnClickListener (this);
        oh.textView.setTag (item);

    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag ();

        onOtherClickListener.onOtherClick (position);
    }

    class OtherHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public OtherHolder(View view) {
            super (view);
            textView = (TextView) view.findViewById (R.id.row_other_name);
        }
    }
}
