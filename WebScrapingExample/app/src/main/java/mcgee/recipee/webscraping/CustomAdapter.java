package mcgee.recipee.webscraping;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends android.widget.ArrayAdapter<Ingredient>{

    private ArrayList<Ingredient> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder{
        TextView txtName;
        TextView txtQuantity;
        CheckBox checkBox;
        TextView delete;
    }

    public CustomAdapter(ArrayList<Ingredient> data, Context context) {
        super(context, R.layout.listitem, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Ingredient ingredient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.txtQuantity = convertView.findViewById(R.id.quantity);
            viewHolder.checkBox = convertView.findViewById(R.id.box);
            viewHolder.delete=convertView.findViewById(R.id.delete_item);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtName.setText(ingredient.getName());
        viewHolder.txtQuantity.setText(ingredient.getQuantity() + " " + ingredient.getMeasurement());
        viewHolder.checkBox.setChecked(ingredient.getChecked());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient.setChecked(!ingredient.getChecked());
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check","i did it");
                dataSet.remove(ingredient);
                notifyDataSetChanged();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}

