package xyz.lavaliva.market.ZakazList;




import java.util.ArrayList;
import java.util.List;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import xyz.lavaliva.market.R;

public class TovarAdapterZakaz extends RecyclerView.Adapter<TovarAdapterZakaz.TovarViewHolder> {

    private List<TovarObjZakaz> zakazObjList = new ArrayList<>();

    @Override
    public TovarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_tovar_v_zakaz, parent, false);
        return new TovarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TovarViewHolder holder, int position) {
        holder.bind(zakazObjList.get(position));
    }

    @Override
    public int getItemCount() {
        return zakazObjList.size();
    }

    public void setItems(List<TovarObjZakaz> tovarObjZakazs) {
        zakazObjList.addAll(tovarObjZakazs);
        notifyDataSetChanged();
    }

    class TovarViewHolder extends RecyclerView.ViewHolder {
        TextView tvNaimenovanie;
        TextView tvCena;
        TextView tvkolihestvo;
        ImageView ivTovar;

        public TovarViewHolder(View itemView) {
            super(itemView);
            tvNaimenovanie = itemView.findViewById(R.id.tvAdresPoDostavke);
            tvCena = itemView.findViewById(R.id.tvCenaOplata);
            tvkolihestvo = itemView.findViewById(R.id.tvKolihestvoOplata);
            ivTovar=itemView.findViewById(R.id.ivTovar);
        }

        public void bind(TovarObjZakaz tovarObjZakaz) {

            tvNaimenovanie.setText(tovarObjZakaz.getNaimenovanie());
            tvCena.setText(tovarObjZakaz.getCena());
            tvkolihestvo.setText(tovarObjZakaz.getKolihestvovtovare());

            Picasso.get().load(tovarObjZakaz.getFoto()).into(ivTovar);        }
    }
}