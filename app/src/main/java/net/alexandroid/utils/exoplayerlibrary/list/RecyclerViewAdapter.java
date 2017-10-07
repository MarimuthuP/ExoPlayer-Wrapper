package net.alexandroid.utils.exoplayerlibrary.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import net.alexandroid.utils.exoplayerlibrary.R;
import net.alexandroid.utils.exoplayerlibrary.exo.ExoPlayer;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final String TEST_TAG_URL = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dskippablelinear&correlator=";

    private final ArrayList<String> mList;
    private RecyclerView mRecyclerView;
    private int currentFirstVisible;

    public RecyclerViewAdapter(ArrayList<String> pList) {
        mList = pList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisible = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
                if (firstVisible != currentFirstVisible) {
                    if (getViewHolder(firstVisible).mExoPlayer != null) {
                        getViewHolder(firstVisible).mExoPlayer.onPausePlayer();
                    }
                    getViewHolder(currentFirstVisible).mExoPlayer.onPlayPlayer();
                    currentFirstVisible = firstVisible;
                }
            }
        });
    }

    private RecyclerViewAdapter.ViewHolder getViewHolder(int position) {
        return (RecyclerViewAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position));
        holder.mExoPlayer = new ExoPlayer.Builder(holder.mExoPlayerView.getContext())
                .setExoPlayerView(holder.mExoPlayerView)
                .setUiControllersVisibility(true)
                .setRepeatModeOn(false)
                .setAutoPlayOn(false)
                .setVideoUrls(mList.get(position))
                .setTagUrl(TEST_TAG_URL)
                //.setExoPlayerEventsListener(this)
                //.setExoAdEventsListener(this)
                //.addSavedInstanceState(savedInstanceState)
                .build();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.mExoPlayer.onInitPlayer();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mExoPlayer.onReleasePlayer();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mExoPlayer.onActivityDestroy();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTextView;
        public final SimpleExoPlayerView mExoPlayerView;
        public ExoPlayer mExoPlayer;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = mView.findViewById(R.id.textView);
            mExoPlayerView = mView.findViewById(R.id.exoPlayerView);
        }
    }


}