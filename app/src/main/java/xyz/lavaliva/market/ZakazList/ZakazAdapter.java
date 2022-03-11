package xyz.lavaliva.market.ZakazList;

import java.util.List;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Model.ILoadMore;
import xyz.lavaliva.market.R;
public class ZakazAdapter extends RecyclerView.Adapter<ZakazAdapter.ZakazViewHolder> {
    List<ZakazObj> zakazObjList ;
    Activity activity;
    boolean isLoading;
    private ILoadMore loadMore;
    int visibleThreshold = 5;
    int lastVisibleItem;
    int totalItemCount;

    public ZakazAdapter(RecyclerView recyclerView, Activity activity, List<ZakazObj> zakazObjsS) {
        this.zakazObjList = zakazObjsS;
        this.activity = activity;
        final LinearLayoutManager linearLayoutManager
                = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public ZakazViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_zakazi, parent, false);
            return new ZakazViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ZakazViewHolder holder, int position) {
        holder.bind(zakazObjList.get(position));
    }
    public void setLoaded() {
        isLoading = false;
    }
    @Override
    public int getItemCount() {
        return zakazObjList.size();
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setItems() {
        notifyDataSetChanged();
    }

    class ZakazViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoZakaza,
                tvVremZakaza,
                tvItogoPoZakazu;
        RecyclerView recyclerViewRow2;

        public ZakazViewHolder(View itemView) {
            super(itemView);
            tvNoZakaza = itemView.findViewById(R.id.tvNoZakaza);
            tvVremZakaza = itemView.findViewById(R.id.tvVremZakaza);
            tvItogoPoZakazu = itemView.findViewById(R.id.tvItogoPoZakazu);
            recyclerViewRow2 = itemView.findViewById(R.id.recZakazTovari);
            recyclerViewRow2.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void bind(ZakazObj zakazObj) {

                    tvNoZakaza.setText(zakazObj.getText());
                    tvVremZakaza.setText(zakazObj.getVremiaZakaza());
                    tvItogoPoZakazu.setText(zakazObj.getItogoPoZakazu());
//                    O.o("Bind v zakaz Adaptere " + zakazObj.getText());
                    List<TovarObjZakaz> tovarList = zakazObj.getTovarObjZakazList();

                    TovarAdapterZakaz tovarAdapterZakaz = new TovarAdapterZakaz();
                    recyclerViewRow2.setAdapter(tovarAdapterZakaz);
                    tovarAdapterZakaz.setItems(tovarList);

            }
        }
    }
