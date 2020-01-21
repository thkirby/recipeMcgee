package us.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.AppDatabase;
import us.ait.android.shoppinglist.data.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbDone;
        public ImageView ivIcon;
        public TextView tvItem;
        public TextView tvDesc;
        public TextView tvPrice;
        public Button btnDelete;
        public Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            cbDone = itemView.findViewById(R.id.cbDone);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    private List<Item> itemsList;
    private Context context;
    private int lastPosition = -1;

    public ItemAdapter(List<Item> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        String price = Double.toString(itemsList.get(position).getEstimatedPrice());
        viewHolder.tvItem.setText(itemsList.get(position).getItemName());
        viewHolder.tvDesc.setText(itemsList.get(position).getDescription());
        viewHolder.tvPrice.setText(price);
        viewHolder.ivIcon.setImageResource(
                itemsList.get(position).getItemTypeAsEnum().getIconId());
        viewHolder.cbDone.setChecked(itemsList.get(position).isBought());
        viewHolder.cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsList.get(position).setBought(viewHolder.cbDone.isChecked());
                new Thread() {
                    @Override
                    public void run() {
                        AppDatabase.getAppDatabase(context).itemDao().update(itemsList.get(position));
                    }
                }.start();
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemDialog(itemsList.get(viewHolder.getAdapterPosition()));
            }
        });
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public double getTotalCost(){
        double totalPrice = 0;
        for (Item item : itemsList){
            totalPrice += item.getEstimatedPrice();
        }
        return totalPrice;
    }

    public void addItem(Item item) {
        itemsList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(Item item) {
        int editPos = findItemIndexByItemId(item.getItemID());
        itemsList.set(editPos, item);
        notifyItemChanged(editPos);
    }

    private int findItemIndexByItemId(long itemID) {
        for (int i = 0; i < itemsList.size(); i++) {
            if (itemsList.get(i).getItemID() == itemID) {
                return i;
            }
        }

        return -1;
    }

    public void removeItem(int position) {
        final Item itemToDelete = itemsList.get(position);
        itemsList.remove(itemToDelete);
        notifyItemRemoved(position);
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).itemDao().delete(
                        itemToDelete);
            }
        }.start();
    }

    public void clear() {
        itemsList.clear();
        notifyDataSetChanged();
    }



    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemsList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemsList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i) {
        return itemsList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
