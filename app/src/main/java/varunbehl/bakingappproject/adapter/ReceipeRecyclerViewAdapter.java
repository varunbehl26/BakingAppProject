package varunbehl.bakingappproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import varunbehl.bakingappproject.MainActivity;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.pojo.BakingData;

public class ReceipeRecyclerViewAdapter extends RecyclerView.Adapter<ReceipeRecyclerViewAdapter.ViewHolder> {

    private final List<BakingData> mValues;
    private final Context context;

    public ReceipeRecyclerViewAdapter(List<BakingData> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipe_bakingdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.receipe_name.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).loadReceipeDetail(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView receipe_name;
        public BakingData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            receipe_name = (TextView) view.findViewById(R.id.receipe_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + receipe_name.getText() + "'";
        }
    }
}
