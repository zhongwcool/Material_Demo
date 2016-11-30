package dean.demo;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean Guo on 10/20/14.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private RecyclerView mWomenList;
    private GridView mWomenGrid;

    private ListAdapter mListAdapter;
    private GridAdapter mGridAdapter;

    private ImageButton button;

    private Context context;

    public static List<Actor> actors = new ArrayList<Actor>();

    private String[] names = {"朱茵", "张柏芝", "张敏", "莫文蔚", "黄圣依", "赵薇", "如花"};

    private String[] pics = {"p1", "p2", "p3", "p4", "p5", "p6", "p7"};

    private String[] works = {"大话西游", "喜剧之王", "逃学威龙", "大话西游", "功夫", "少林足球", "唐伯虎点秋香"};

    private String[] role = {"紫霞仙子", "柳飘飘", "女友", "白晶晶", "哑女", "阿梅", "如花"};

    private String[][] picGroups = {{"p1","p1_1", "p1_2", "p1_3"},{"p2","p2_1", "p2_2", "p2_3"},{"p3"},{"p4"},{"p5"},{"p6"},{"p7"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set Explode enter transition animation for current activity
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setDuration(1000));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init data
        this.context = this;
        getSupportActionBar().setTitle("那些年我们追过的星女郎");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mWomenGrid = (GridView) findViewById(R.id.grid);
            mWomenGrid.setNumColumns(2);

            mGridAdapter = new GridAdapter(this, actors);
            mWomenGrid.setAdapter(mGridAdapter);
            mWomenGrid.setOnItemClickListener(this);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // init RecyclerView
            mWomenList = (RecyclerView) findViewById(R.id.list);
            mWomenList.setLayoutManager(new LinearLayoutManager(this));
            mWomenList.setItemAnimator(new DefaultItemAnimator());

            // set adapter
            mListAdapter = new ListAdapter(this, actors);
            mWomenList.setAdapter(mListAdapter);
        }

        // set outline and listener for floating action button
        button = (ImageButton) this.findViewById(R.id.add_button);
        button.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int shapeSize = (int) getResources().getDimension(R.dimen.shape_size);
                outline.setRoundRect(0, 0, shapeSize, shapeSize, shapeSize / 2);
            }
        });
        button.setClipToOutline(true);
        button.setOnClickListener(new View.OnClickListener() {
            boolean isAdd = true;
            int count = -1;

            @Override
            public void onClick(View v) {
                // start animation
                Animator animator = createAnimation(v);
                animator.start();

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // add item
                    count = mListAdapter.getItemCount();
                    if (count != names.length && isAdd) {
                        //int size = count;
                        actors.add(new Actor(names[count], pics[count], works[count], role[count], picGroups[count]));
                        mWomenList.scrollToPosition(count - 1);
                        mListAdapter.notifyDataSetChanged();
                    }
                    // delete item
                    else {
                        actors.remove(count - 1);
                        mWomenList.scrollToPosition(count - 1);
                        mListAdapter.notifyDataSetChanged();
                    }

                    count = mListAdapter.getItemCount();

                    if (mListAdapter.getItemCount() == 0) {
                        button.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
                        isAdd = true;
                    }
                    if (mListAdapter.getItemCount() == names.length) {
                        button.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
                        isAdd = false;
                    }
                }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // add item
                    count = mGridAdapter.getCount();
                    if (count != names.length && isAdd) {
                        //int size = count;
                        actors.add(new Actor(names[count], pics[count], works[count], role[count], picGroups[count]));
                        mWomenGrid.setVerticalScrollbarPosition(count - 1);
                        mGridAdapter.notifyDataSetChanged();
                    }
                    // delete item
                    else {
                        actors.remove(count - 1);
                        mWomenGrid.setVerticalScrollbarPosition(count - 1);
                        mGridAdapter.notifyDataSetChanged();
                    }

                    count = mGridAdapter.getCount();

                    if (count == 0) {
                        button.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
                        isAdd = true;
                    }
                    if (count == names.length) {
                        button.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
                        isAdd = false;
                    }
                }
            }
        });

    }

    /**
     * start detail activity
     */
    public void startActivity(final View v, final int position) {

        View pic = v.findViewById(R.id.pic);
        View add_btn = this.findViewById(R.id.add_button);

        // set share element transition animation for current activity
        Transition ts = new ChangeTransform();
        ts.setDuration(3000);
        getWindow().setExitTransition(ts);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                Pair.create(pic, position + "pic"),
                Pair.create(add_btn, "ShareBtn")).toBundle();

        // start activity with share element transition
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("pos", position);
        startActivity(intent, bundle);

    }

    /**
     * create CircularReveal animation
     */
    public Animator createAnimation(View v) {
        // create a CircularReveal animation
        Animator animator = ViewAnimationUtils.createCircularReveal(
                v,
                v.getWidth() / 2,
                v.getHeight() / 2,
                0,
                v.getWidth());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        return animator;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //android.util.Log.e("ALEX", "onItemClick 事件");
        startActivity(view, position);
    }
}
