package org.cantabile.douyin.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cantabile.douyin.R;

/**
 * Created by simple on 2017/12/14.
 */

/**
 *  常规 toolbar
 *  改进：换掉固定icon， 开放自定义属性显示图片，开放点击接口，降低耦合度 本次项目不在改进，后期项目在做
 */
public class CommToolBar extends LinearLayout{

    private Context mContext;

    private ImageView iconBack;
    private TextView tvTitle;
    private ImageView iconOptionsSortType;
    private ImageView iconOptionsSearch;
    private ImageView iconOptionsOption;
    private TextView tvOptionClear;
    private CommToolBarOnClickListener listener;

    private int optionSortVisibility, optionSearchVisibility, optionOptionVisibility, optionClearVisibility;
    private String title;

    public CommToolBar(Context context) {
        this(context, null);
    }

    public CommToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommToolBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        initView();
        initEvent();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CommToolBar);
        optionSortVisibility = typedArray.getInt(R.styleable.CommToolBar_option_sort, GONE);
        optionSearchVisibility = typedArray.getInt(R.styleable.CommToolBar_option_search, GONE);
        optionOptionVisibility = typedArray.getInt(R.styleable.CommToolBar_option_option, GONE);
        optionClearVisibility = typedArray.getInt(R.styleable.CommToolBar_option_clear, GONE);
        title = typedArray.getString(R.styleable.CommToolBar_title);
        typedArray.recycle();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_comm_tool_bar, this, true);
        iconBack = (ImageView) view.findViewById(R.id.iconBack);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        iconOptionsSortType = (ImageView) view.findViewById(R.id.iconOptionsSortType);
        iconOptionsSearch = (ImageView) view.findViewById(R.id.iconOptionsSearch);
        iconOptionsOption = (ImageView) view.findViewById(R.id.iconOptionsOption);
        tvOptionClear = (TextView) view.findViewById(R.id.tvOptionClear);

        iconOptionsSortType.setVisibility(optionSortVisibility);
        iconOptionsSearch.setVisibility(optionSearchVisibility);
        iconOptionsOption.setVisibility(optionOptionVisibility);
        tvOptionClear.setVisibility(optionClearVisibility);
    }

    private void initEvent() {

        iconBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
            }
        });
        tvTitle.setText(title);
        iconOptionsSortType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onSortClick();
            }
        });
        iconOptionsSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onSearchClick();
            }
        });
        iconOptionsOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onOptionClick();
            }
        });
        tvOptionClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClearClick();
            }
        });
    }

    public void setListener(CommToolBarOnClickListener listener) {
        this.listener = listener;
    }

    public interface CommToolBarOnClickListener{
        public  void onSortClick();
        public  void onSearchClick();
        public  void onOptionClick();
        public  void onClearClick();
    }
}
