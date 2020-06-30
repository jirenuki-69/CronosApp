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
import cronos.com.cronosapp.Models.Grupo;
import cronos.com.cronosapp.R;

class CustomViewHolderGrupo extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView num_grupo;
    public TextView carrera;
    public RelativeLayout tarjeta_grupo;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomViewHolderGrupo(View itemView) {
        super(itemView);
        num_grupo = (TextView) itemView.findViewById(R.id.txvnum_grupotj);
        carrera = (TextView) itemView.findViewById(R.id.txvCarrerasemestretj);
        tarjeta_grupo = (RelativeLayout) itemView.findViewById(R.id.rltarjeta_grupo);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}

public class GrupoAdapter extends RecyclerView.Adapter<CustomViewHolderGrupo>{

    int row_index = -1;
    Context context;
    List<Grupo> grupo_list;

    public GrupoAdapter(List<Grupo> lista_seat, Context context){
        super();
        //Getting all plant
        this.grupo_list = lista_seat;
        this.context = context;
    }

    @Override
    public CustomViewHolderGrupo onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.tarjeta_grupos,parent,false);
        return new CustomViewHolderGrupo(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolderGrupo holder, int position) {

        holder.num_grupo.setText("" + grupo_list.get(position).getNum_grupo());

        holder.carrera.setText(grupo_list.get(position).getCarrera() + " Semestre: " + grupo_list.get(position).getSemestre());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                row_index = position;
                Common.currentItemGrupo = grupo_list.get(position);
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
        return grupo_list.size();
    }
}

