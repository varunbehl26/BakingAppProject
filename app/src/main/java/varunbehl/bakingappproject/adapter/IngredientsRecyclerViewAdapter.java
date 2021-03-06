package varunbehl.bakingappproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.pojo.Ingredient;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final Context context;

    public IngredientsRecyclerViewAdapter(Context context, List<Ingredient> ingredients) {
        mValues = ingredients;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bakingdataingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.ingredient.setText(mValues.get(position).getIngredient());
        holder.measure.setText(mValues.get(position).getMeasure());
        holder.quantity.setText(mValues.get(position).getQuantity().toString());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView ingredient;
        public final TextView quantity;
        public final TextView measure;
        public Ingredient mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ingredient = (TextView) view.findViewById(R.id.ingredient);
            quantity = (TextView) view.findViewById(R.id.quantity);
            measure = (TextView) view.findViewById(R.id.measure);
        }


    }
}
