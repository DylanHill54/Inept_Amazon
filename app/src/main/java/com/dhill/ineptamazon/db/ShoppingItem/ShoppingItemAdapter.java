package com.dhill.ineptamazon.db.ShoppingItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dhill.ineptamazon.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemHolder> implements Filterable {
    private List<ShoppingItem> shoppingItems= new ArrayList<>();
    private List<ShoppingItem> shoppingItemsMaster;
   private OnItemCLickListener listener;

    /*public ShoppingItemAdapter(){
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ShoppingItem> DIFF_CALLBACK=new DiffUtil.ItemCallback<ShoppingItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
           return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getSupply() == newItem.getSupply();
        }
    };*/
    @NonNull
    @Override
    public ShoppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item, parent, false);
        return new ShoppingItemHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ShoppingItemHolder holder, int position) {
        ShoppingItem currentShoppingItem = shoppingItems.get(position);
        holder.textViewName.setText(currentShoppingItem.getName());
        holder.textViewDescription.setText(currentShoppingItem.getDescription());
        holder.textViewSupply.setText(String.valueOf(currentShoppingItem.getSupply()));
    }
    public void setShoppingItems(List<ShoppingItem>shoppingItems){
        this.shoppingItems=shoppingItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();

    }

    public ShoppingItem getShoppingItemAt(int position) {
        return shoppingItems.get(position);
    }

    public void setOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
    }

    class ShoppingItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewSupply;
        public ShoppingItemHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewSupply = itemView.findViewById(R.id.text_view_supply);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= getAdapterPosition();
                    if(listener!=null&& position!= RecyclerView.NO_POSITION)
                        listener.onItemCLick(shoppingItems.get(position));
                }
            });
        }
    }
    public interface OnItemCLickListener{
        void onItemCLick(ShoppingItem shoppingItem);
    }
    public void setOnItemClickListener(OnItemCLickListener listener){
        this.listener=listener;

    }


    @Override
    public Filter getFilter() {
        return itemFilter;
    }
    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShoppingItem> filteredList=new ArrayList<>();
            if(constraint==null||constraint.length()==0){
                filteredList.addAll(shoppingItemsMaster);
            }else{
                String filterPattern=constraint.toString().toLowerCase().trim();

                for(ShoppingItem item: shoppingItemsMaster){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shoppingItems.clear();
            shoppingItems.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
