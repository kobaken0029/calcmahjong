package eval.wit.ai.calcmahjong.models.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import eval.wit.ai.calcmahjong.R;
import eval.wit.ai.calcmahjong.activities.WikipediaWebViewActivity;
import eval.wit.ai.calcmahjong.models.adapter.SimpleDrawerAdapter.ViewHolder;
import eval.wit.ai.calcmahjong.resources.Consts;


/**
 * Created by koba on 2015/03/17.
 */
public class SimpleDrawerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;

    // MainActivityから渡されるデータ
    private String[] mDrawerMenuArr;

    /**
     * Drawerのホルダークラス。
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View itemView, int viewType, Context c) {
            super(itemView);

            //各アイテムのViewを取得
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            mTextView.setTextColor(c.getResources().getColor(R.color.black_semi_transparent));

            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public SimpleDrawerAdapter(String[] arrayList, Context context) {
        mDrawerMenuArr = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // レイアウトはsimple_list_item_1を利用
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer, parent, false);

        // アイテム選択可能にする
        itemView.setClickable(true);

        // アイテム選択時の Ripple Drawable を有効にする
        // Android 4 系端末で確認すると、Ripple効果は付かないが、選択色のみ適用される
        TypedValue outValue = new TypedValue();
        parent.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        itemView.setBackgroundResource(outValue.resourceId);

        return new ViewHolder(itemView, viewType, context.getApplicationContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // 各アイテムのViewに、データをバインドする
        String menu = mDrawerMenuArr[position];
        holder.mTextView.setText(menu);
        holder.mTextView.setOnClickListener(listener(menu));

        switch (menu) {
            case Consts.MAHJONG_YAKU_LIST :
                holder.mImageView.setImageResource(R.drawable.ic_mahjong_yaku_list);
                break;
            case Consts.MAHJONG_POINT_LIST :
                holder.mImageView.setImageResource(R.drawable.ic_mahjong_point_list);
                break;
            case Consts.MAHJONG_RULES :
                holder.mImageView.setImageResource(R.drawable.ic_mahjong_rules);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerMenuArr.length;
    }


    View.OnClickListener listener(final String menu) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WikipediaWebViewActivity.class);
                intent.putExtra("menu", menu);
                context.startActivity(intent);
            }
        };
    }
}
