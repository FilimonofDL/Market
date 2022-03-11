package xyz.lavaliva.market.Poisk;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Model.ILoadMore;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.ResPoiskaStranica.ResPoiskaStranicaActivity;
import xyz.lavaliva.market.TovarPage.TovarPageActivity;


public class PoiskAdapter extends RecyclerView.Adapter<PoiskAdapter.PoiskTextViewHolder> {
    List<PoiskTextObj> poiskTextObjs;
    Activity activity;
    boolean isLoading;
    private ILoadMore loadMore;
    int visibleThreshold = 5;
    int lastVisibleItem;
    int totalItemCount;
    public static PoiskTextObj poiskTextObjVibraniy;
    static boolean loadHistory = true;

    final ForegroundColorSpan style = new ForegroundColorSpan(Color.rgb(255, 0, 0));
    final ForegroundColorSpan style2 = new ForegroundColorSpan(Color.rgb(0, 0, 255));
//    public  static TovariObjPrilavok tovariObjPrilavokStatic;


    public PoiskAdapter(RecyclerView recyclerView, Activity activity,
                        List<PoiskTextObj> poiskTextObjs) {

        this.activity = activity;
        this.poiskTextObjs = poiskTextObjs;
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
    public PoiskTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_text_v_poiske, parent, false);
        return new PoiskTextViewHolder(view);


    }

    @Override
    public void onBindViewHolder(PoiskTextViewHolder holder, int position) {
        holder.bind(poiskTextObjs.get(position));
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return poiskTextObjs.size();
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setItems() {
        notifyDataSetChanged();
    }

    class PoiskTextViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoidenoVPoiske;
        View view;


        public PoiskTextViewHolder(View itemView) {
            super(itemView);

            tvNoidenoVPoiske = itemView.findViewById(R.id.tvNoidenoVPoiske);
            view = itemView;


        }

        public void bind(final PoiskTextObj poiskTextObj) {

            if (loadHistory) {
                tvNoidenoVPoiske.setText(poiskTextObj.getText());
                tvNoidenoVPoiske.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        poiskTextObjVibraniy = poiskTextObj;
                        createIntentPageTovara();
                    }
                });
            } else {
                okrahivanieTextaVKrasniy(poiskTextObj.getText(), tvNoidenoVPoiske);
                tvNoidenoVPoiske.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        poiskTextObjVibraniy = poiskTextObj;
                        createIntentPageTovara();
                    }
                });
            }

        }
    }

    private void okrahivanieTextaVKrasniy(String textResultatZaprosa, TextView tvNoidenoVPoiske) {

        tvNoidenoVPoiske.setText("");
        String x = PoiskActivity.iskomiyText;
        for (int i = 0; i < textResultatZaprosa.length() + 1 - x.length(); i++) {
            String y = "";
            for (int z = 0; z < x.length(); z++) {
                y = y + textResultatZaprosa.charAt(i + z);
            }
            if (x.equalsIgnoreCase(y) && i > 0 && textResultatZaprosa.charAt(i - 1) == ' ') {
                i = dobavlenieBukvDliaCiklaPerebora(i, y, tvNoidenoVPoiske);
            } else if (x.equalsIgnoreCase(y) & i == 0) {
                i = dobavlenieBukvDliaCiklaPerebora(i, y, tvNoidenoVPoiske);
            } else {
                tvNoidenoVPoiske.append(String.valueOf(y.charAt(0)));
            }
        }
        if (tvNoidenoVPoiske.length() != textResultatZaprosa.length()) {
            System.out.println(tvNoidenoVPoiske.length() + " " + textResultatZaprosa.length());
            for (int d = tvNoidenoVPoiske.length(); d < textResultatZaprosa.length(); d++) {
                tvNoidenoVPoiske.append(Character.toString(textResultatZaprosa.charAt(d)));
            }
        }
    }

    private int dobavlenieBukvDliaCiklaPerebora(int i, String x, TextView tvNoidenoVPoiske) {
        i = i + x.length() - 1;
        Spannable temp = new SpannableString(x);
        temp.setSpan(new ForegroundColorSpan(Color.RED), 0, x.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNoidenoVPoiske.append(temp);
        return i;
    }

    public  void createIntentPageTovara() {
        Intent intentPageTovara = new Intent(activity, ResPoiskaStranicaActivity.class);

        activity.startActivity(intentPageTovara);
    }
}