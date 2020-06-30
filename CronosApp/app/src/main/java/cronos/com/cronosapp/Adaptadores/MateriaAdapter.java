package cronos.com.cronosapp.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cronos.com.cronosapp.Helpers.Common;
import cronos.com.cronosapp.Helpers.ItemClickListener;
import cronos.com.cronosapp.Models.Materia;
import cronos.com.cronosapp.R;

class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView num_materia;
    public TextView nombre_materia;
    public RelativeLayout tarjeta_materia;

    ItemClickListener itemClickListener;



    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomViewHolder(View itemView) {
        super(itemView);
        num_materia = (TextView) itemView.findViewById(R.id.txvNumMateria);
        nombre_materia = (TextView) itemView.findViewById(R.id.txvNombreMateria);
        tarjeta_materia = (RelativeLayout) itemView.findViewById(R.id.rltarjeta_materia);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}

public class MateriaAdapter extends RecyclerView.Adapter<CustomViewHolder>{

    int row_index = -1;
    Context context;
    List<Materia> materia_list;

    public MateriaAdapter(List<Materia> lista_seat, Context context){
        super();
        //Getting all plant
        this.materia_list = lista_seat;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.tarjeta_materias,parent,false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.num_materia.setText("" + materia_list.get(position).getNum_materia());

        holder.nombre_materia.setText(materia_list.get(position).getNombre_materia());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;
                Common.currentItemMateria = materia_list.get(position);
                notifyDataSetChanged();
            }
        });

        if (row_index == position){
            holder.tarjeta_materia.setBackgroundColor(Color.parseColor("#5D9E96"));
        }else{
            holder.tarjeta_materia.setBackgroundColor(Color.parseColor("#2B14A2"));
        }
    }

    @Override
    public int getItemCount() {
        return materia_list.size();
    }
}
