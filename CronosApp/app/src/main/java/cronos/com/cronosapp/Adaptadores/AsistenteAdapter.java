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
import cronos.com.cronosapp.Models.Asistente;
import cronos.com.cronosapp.Models.Clase;
import cronos.com.cronosapp.R;

class CustomViewHolderAsistente extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView todoclase;
    public RelativeLayout tarjeta_grupo;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomViewHolderAsistente(View itemView) {
        super(itemView);
        todoclase = (TextView) itemView.findViewById(R.id.txvTodoAsistente);
        tarjeta_grupo = (RelativeLayout) itemView.findViewById(R.id.rltarjeta_asistente);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}

public class AsistenteAdapter extends RecyclerView.Adapter<CustomViewHolderAsistente>{

    int row_index = -1;
    Context context;
    List<Asistente> asistente_list;

    public AsistenteAdapter(List<Asistente> lista_seat, Context context){
        super();
        //Getting all plant
        this.asistente_list = lista_seat;
        this.context = context;
    }

    @Override
    public CustomViewHolderAsistente onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.tarjeta_asistente,parent,false);
        return new CustomViewHolderAsistente(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolderAsistente holder, int position) {
        holder.todoclase.setText("" + asistente_list.get(position).getMatricula() + " - " +asistente_list.get(position).getNombre() + " " + asistente_list.get(position).getApellidos());
    }

    @Override
    public int getItemCount() {
        return asistente_list.size();
    }
}
