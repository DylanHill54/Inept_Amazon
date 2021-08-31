package com.dhill.ineptamazon.db.ShoppingCart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dhill.ineptamazon.R;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartHolder> {
    private ShoppingCartDAO shoppingCartDAO;
    private List<ShoppingCart> shoppingCarts=new ArrayList<>();
    private List<ShoppingCart> shoppingCartsMaster;

    private ShoppingCartAdapter.OnItemCLickListener listener;

    /*public ShoppingCartAdapter(){
        super(DIFF_CALLBACK);
    }
   *//* public List<ShoppingCart> getFilteredList(int userId){
        exampleList= shoppingCartDAO.getAllShoppingCartsByUserId(userId);
        return exampleList;
        was trying to filter the list i passed into the adapter but it refused to wo

    }*//*

    private static final DiffUtil.ItemCallback<ShoppingCart> DIFF_CALLBACK=new DiffUtil.ItemCallback<ShoppingCart>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingCart oldItem, @NonNull ShoppingCart newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingCart oldItem, @NonNull ShoppingCart newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getSupply() == newItem.getSupply();
        }
    };*/
    @NonNull
    @Override
    public ShoppingCartAdapter.ShoppingCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout, parent, false);
        return new ShoppingCartAdapter.ShoppingCartHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartAdapter.ShoppingCartHolder holder, int position) {
        ShoppingCart currentShoppingCart = shoppingCarts.get(position);
        holder.textViewName.setText(currentShoppingCart.getName());
        holder.textViewDescription.setText(currentShoppingCart.getDescription());
        holder.textViewSupply.setText(String.valueOf(currentShoppingCart.getSupply()));
        holder.textViewUser.setText(currentShoppingCart.getUserId());
    }
    public void setShoppingCarts(List<ShoppingCart>shoppingCarts){
        this.shoppingCarts=shoppingCarts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public ShoppingCart getShoppingCartAt(int position) {
        return shoppingCarts.get(position);
    }

    class ShoppingCartHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewSupply;
        private TextView textViewUser;
        public ShoppingCartHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewSupply = itemView.findViewById(R.id.text_view_supply);
            textViewUser=itemView.findViewById(R.id.text_view_user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= getAdapterPosition();
                    if(listener!=null&& position!= RecyclerView.NO_POSITION)
                        listener.onItemCLick(shoppingCarts.get(position));
                }
            });
        }
    }


    public interface OnItemCLickListener{
        void onItemCLick(ShoppingCart shoppingCart);
    }
    public void setOnItemClickListener(ShoppingCartAdapter.OnItemCLickListener listener){
        this.listener=listener;

    }




}

