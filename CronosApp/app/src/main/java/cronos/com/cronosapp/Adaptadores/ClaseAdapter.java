package cronos.com.cronosapp.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.ItemClickListener;
import cronos.com.cronosapp.Models.Clase;
import cronos.com.cronosapp.Models.Grupo;
import cronos.com.cronosapp.R;

class CustomViewHolderClase extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView todoclase;
    public TextView idclase;
    public RelativeLayout tarjeta_grupo;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomViewHolderClase(View itemView) {
        super(itemView);
        todoclase = (TextView) itemView.findViewById(R.id.txvTodoClase);
        idclase = (TextView) itemView.findViewById(R.id.txvidclase);
        tarjeta_grupo = (RelativeLayout) itemView.findViewById(R.id.rltarjeta_clase);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}

public class ClaseAdapter extends RecyclerView.Adapter<CustomViewHolderClase>{

    int row_index = -1;
    Context context;
    List<Clase> clase_list;

    public ClaseAdapter(List<Clase> lista_seat, Context context){
        super();
        //Getting all plant
        this.clase_list = lista_seat;
        this.context = context;
    }

    @Override
    public CustomViewHolderClase onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.tarjeta_clase,parent,false);
        return new CustomViewHolderClase(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolderClase holder, int position) {

        holder.todoclase.setText("" + clase_list.get(position).getHora_generadaqr());

        holder.idclase.setText("" + clase_list.get(position).getId_clase());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;
                Common.currentItemClase = clase_list.get(position);
                notifyDataSetChanged();
            }
        });

        if (row_index == position){
            holder.tarjeta_grupo.setBackgroundColor(Color.parseColor("#a35ca0"));
        }else{
            holder.tarjeta_grupo.setBackgroundColor(Color.parseColor("#5D9E96"));
        }
    }

    @Override
    public int getItemCount() {
        return clase_list.size();
    }
}
