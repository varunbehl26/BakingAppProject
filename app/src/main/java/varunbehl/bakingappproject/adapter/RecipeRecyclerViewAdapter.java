package varunbehl.bakingappproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.activity.MainActivity;
import varunbehl.bakingappproject.pojo.BakingData;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final List<BakingData> mValues;
    private final Context context;

    public RecipeRecyclerViewAdapter(List<BakingData> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_bakingdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.Recipe_name.setText(mValues.get(position).getName());
        holder.draweeView.setImageURI(mValues.get(position).getImage());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).loadRecipeDetail(holder.mItem);
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
        public final TextView Recipe_name;
        public BakingData mItem;
        SimpleDraweeView draweeView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            Recipe_name = (TextView) view.findViewById(R.id.Recipe_name);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.Recipe_img);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + Recipe_name.getText() + "'";
        }
    }
}
