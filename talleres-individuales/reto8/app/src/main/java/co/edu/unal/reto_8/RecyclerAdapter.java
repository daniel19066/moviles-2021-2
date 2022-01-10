package co.edu.unal.reto_8;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombretv,urlwebtv,telefonotv,emailtv,producservtv,clasificaciontv;
        ImageView buildtv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombretv = (TextView)itemView.findViewById(R.id.textViewnom);
            urlwebtv = (TextView)itemView.findViewById(R.id.textViewurl);
            telefonotv = (TextView)itemView.findViewById(R.id.textViewtel);
            emailtv = (TextView)itemView.findViewById(R.id.textViewemail);
            producservtv = (TextView)itemView.findViewById(R.id.textViewprod);
            clasificaciontv = (TextView)itemView.findViewById(R.id.textViewclas);
            buildtv = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public List<EmpresaModelo> empresaLista;

    public RecyclerAdapter(List<EmpresaModelo> empresaLista) {
        this.empresaLista = empresaLista;
    }

    //@NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresas,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombretv.setText(empresaLista.get(position).getNombre());
        holder.urlwebtv.setText(empresaLista.get(position).getUrlweb());
        holder.telefonotv.setText(empresaLista.get(position).getTelefono());
        holder.emailtv.setText(empresaLista.get(position).getEmail());
        holder.producservtv.setText(empresaLista.get(position).getProduServ());
        holder.clasificaciontv.setText(empresaLista.get(position).getClasifica());
        holder.buildtv.setImageResource(R.drawable.build);
    }

    public int getItemCount(){
        return empresaLista.size();
    }
}
