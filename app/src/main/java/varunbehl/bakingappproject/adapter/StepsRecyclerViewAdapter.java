package varunbehl.bakingappproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.activity.MainActivity;
import varunbehl.bakingappproject.pojo.BakingData;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private final BakingData mValues;
    private final Context mContext;

    public StepsRecyclerViewAdapter(BakingData mValues, Context context) {
        this.mValues = mValues;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bakingdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues;
        holder.description.setText(mValues.getSteps().get(position).getShortDescription());
        holder.thumbnaiView.setImageURI(mValues.getSteps().get(position).getThumbnailURL());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).onStepsClick(mValues, holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.getSteps().size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView description;
        public BakingData mItem;
        SimpleDraweeView thumbnaiView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            description = (TextView) view.findViewById(R.id.description);
            thumbnaiView = (SimpleDraweeView) view.findViewById(R.id.step_img);
        }

    }
}
