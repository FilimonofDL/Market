package xyz.lavaliva.market.ResPoiskaStranica;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.ILoadMore;
import Model.O;
import Model.Utils;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;
import xyz.lavaliva.market.TovarList.TovariObjPrilavok;
import xyz.lavaliva.market.TovarPage.TovarPageActivity;

import static xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok.korzinaCount1;

public class TovariAdapterResPoiska extends RecyclerView.Adapter<TovariAdapterResPoiska.TovarViewHolder> {
    List<TovariObjPrilavok> tovarObjList;
    Activity activity;
    boolean isLoading;
    private ILoadMore loadMore;
    int visibleThreshold = 5;
    int lastVisibleItem;
    int totalItemCount;


    public TovariAdapterResPoiska(RecyclerView recyclerView, Activity activity,
                                  List <TovariObjPrilavok> tovariObjPrilavoks) {

//        O.o("constr adapter");
        this.activity = activity;
        this.tovarObjList = tovariObjPrilavoks;
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
    public TovarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_tovar, parent, false);
//        O.o("inflate poisk");
        return new TovarViewHolder(view);


    }

    @Override
    public void onBindViewHolder(TovarViewHolder holder, int position) {
//        O.o("Bind poisk");
        holder.bind(tovarObjList.get(position));
    }
    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return tovarObjList.size();
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setItems() {
        notifyDataSetChanged();
    }

    class TovarViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkidkaTov,
                tvNaimenovanie
                ,tvCenaBezSkidki,
                textView7,
                tvKKorzine,
                tvCenaSoSkidkoy
                        ;
        ImageButton ibKupit;
        ImageView ivTovar;
        View view;



        public TovarViewHolder(View itemView) {
            super(itemView);
//            O.o("tv set naimenov");

            tvSkidkaTov = itemView.findViewById(R.id.tvSkidkaTov);
            tvNaimenovanie = itemView.findViewById(R.id.tvAdresPoDostavke);
            tvCenaBezSkidki = itemView.findViewById(R.id.tvSposobDostavki);
            textView7 = itemView.findViewById(R.id.textView7);
            tvCenaSoSkidkoy = itemView.findViewById(R.id.tvCenaSoSkidkoy);
            ibKupit = itemView.findViewById(R.id.ibKupit);
            ivTovar = itemView.findViewById(R.id.ivTovar);
            tvKKorzine = itemView.findViewById(R.id.tvKKorzine);
//
//            tvKKorzine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    SpisokTovarovPrilavok.ibKorzina.performClick();}
//            });
//            view=itemView;


        }

        public void bind(final TovariObjPrilavok tovariObjPrilavok) {
//            O.o("bind "+tovariObjPrilavok.getNaimenovanie());



            tvSkidkaTov.setText(Integer.toString(tovariObjPrilavok.getKolihestvo()));
            tvSkidkaTov.setText(Integer.toString(tovariObjPrilavok.getSkidka()));
            tvNaimenovanie.setText(tovariObjPrilavok.getNaimenovanie());
            tvCenaBezSkidki.setText(String.format("%.2f",tovariObjPrilavok.getCenaBezSkidki()));
            tvCenaBezSkidki.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            textView7.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tovariObjPrilavokStatic=tovariObjPrilavok;
////                    ID_TOVARA_OTKRITOGO=tovariObjPrilavok.getId();
//                    createIntentPageTovara();
//                }
//            });
            if ((tovariObjPrilavok.getCenaSoSkidkoy()>0)) {
                String skidkaIzDvuxCen = "СКИДКА " + String.format("%, (.0f", 100- tovariObjPrilavok.getCenaSoSkidkoy()/
                        tovariObjPrilavok.getCenaBezSkidki()*100  ) + "%";
                tvSkidkaTov.setText(skidkaIzDvuxCen);
                tvCenaBezSkidki.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
                tvCenaSoSkidkoy.setText(tovariObjPrilavok.getCenaSoSkidkoy().toString());


            }else if (tovariObjPrilavok.getSkidka()>0){
                tvSkidkaTov.setText("СКИДКА " +String.valueOf(tovariObjPrilavok.getSkidka())+ "%");
                tvCenaBezSkidki.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
                Double cenaSoSkidkoy= tovariObjPrilavok.getCenaBezSkidki()- tovariObjPrilavok.getCenaBezSkidki()*
                        tovariObjPrilavok.getSkidka()/100;
                tvCenaSoSkidkoy.setText(cenaSoSkidkoy.toString());
            }else {
                tvSkidkaTov.setVisibility(View.GONE);
                tvCenaBezSkidki.setVisibility(View.GONE);
                textView7.setVisibility(View.GONE);
                tvCenaSoSkidkoy.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
            }
            if(tovariObjPrilavok.getKolihestvo()>0){
//                O.o(Integer.toString(tovariObjPrilavok.getKolihestvo())+" "+tovariObjPrilavok.getId()+" Zelenaya knopka");
                ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
                tvKKorzine.setVisibility(View.VISIBLE);
            }

            tvCenaSoSkidkoy.setText(tovariObjPrilavok.getCenaSoSkidkoy().toString());

//            ibKupit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    SpisokTovarovPrilavok.tvKorzinaCount.setVisibility(View.VISIBLE);
//                    korzinaCount1++;
//                    SpisokTovarovPrilavok.tvKorzinaCount.setText(Integer.toString(korzinaCount1));
//
//                    if(!tovariObjPrilavok.isSelected()) {
//                        tvKKorzine.setVisibility(View.VISIBLE);
//                    }
//                    if(korzinaCount1==0){
//                        SpisokTovarovPrilavok.tvKorzinaCount.setVisibility(View.VISIBLE);
//                    }
//                    ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
//                    tvKKorzine.setVisibility(View.VISIBLE);
//                    SpisokTovarovPrilavok.tvKorzinaCount.setText(String.valueOf(korzinaCount1));
//                    buySQL(tovariObjPrilavok.getId() );
//                }
//            });
            Picasso.get()
                    .load(tovariObjPrilavok.getFoto_url())
                    .into(ivTovar);


        }
    }

    private void createIntentPageTovara( ) {
        Intent intentPageTovara=new Intent(activity, TovarPageActivity.class);
//        intentPageTovara.putExtra(ID_TOVARA_OTKRITOGO, (Serializable) tovariObjPrilavok);
        activity.startActivity(intentPageTovara);
    }


    public static void buySQL (final String tovar){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();

                parameters.put("tovar", tovar);
                parameters.put("pokupatel", SpisokTovarovPrilavok.userId);
                return parameters;
            }
        };

        SpisokTovarovPrilavok.requestQueue.add(stringRequest);
    }
}
//public class TovariAdapterPrilavok extends RecyclerView.Adapter<TovariAdapterPrilavok.ViewHolder> {
//
//    private List<TovariObjPrilavok> tovariObjPrilavoks;
//    private Context context;
//    TextView tvKorzinaCount1;
//    int korzinaCount1;
//    String userId1;
//
//
//    public TovariAdapterPrilavok(List<TovariObjPrilavok> tovariObjPrilavoks, Context context, TextView tvKorzinaCount, int korzinaCount,
//                         String userId) {
//
//        // generate constructors to initialise the List and Context objects
//
//        this.tovariObjPrilavoks = tovariObjPrilavoks;
//        this.context = context;
//        tvKorzinaCount1=tvKorzinaCount;
//        korzinaCount1=korzinaCount;
//        if(korzinaCount1>0){
//            tvKorzinaCount1.setVisibility(View.VISIBLE);
//
//            tvKorzinaCount.setText(String.valueOf(korzinaCount1));
//        }
//        userId1=userId;
//
//
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // this method will be called whenever our ViewHolder is created
//
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.row_tovar, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//
//        // this method will bind the data to the ViewHolder from whence it'll be shown to other Views
//
//        final TovariObjPrilavok tovariObjPrilavok = tovariObjPrilavoks.get(position);
//        holder.tvCenaBezSkidki.setPaintFlags(holder.tvCenaBezSkidki.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//              if((tovariObjPrilavok.getCenaSoSkidkoy()>0)){
//                  String skidkaIzDvuxCen = "СКИДКА " + String.format("%, (.0f", 100- tovariObjPrilavok.getCenaSoSkidkoy()/
//                          tovariObjPrilavok.getCenaBezSkidki()*100  ) + "%";
//                  holder.tvSkidka.setText(skidkaIzDvuxCen);
//                  holder.tvCenaBezSkidki.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
//                  holder.tvCenaSoSkidkoy.setText(tovariObjPrilavok.getCenaSoSkidkoy().toString());
//
//
//        }else if (tovariObjPrilavok.getSkidka()>0){
//                  holder.tvSkidka.setText("СКИДКА " +String.valueOf(tovariObjPrilavok.getSkidka())+ "%");
//                  holder.tvCenaBezSkidki.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
//                  Double cenaSoSkidkoy= tovariObjPrilavok.getCenaBezSkidki()- tovariObjPrilavok.getCenaBezSkidki()*
// tovariObjPrilavok.getSkidka()/100;
//                  holder.tvCenaSoSkidkoy.setText(cenaSoSkidkoy.toString());
//        }else {
//            holder.tvSkidka.setVisibility(View.GONE);
//            holder.conlayCenaBezSkidki.setVisibility(View.GONE);
//            holder.tvCenaSoSkidkoy.setText(tovariObjPrilavok.getCenaBezSkidki().toString());
//        }
//        holder.tvNaimenovanie.setText(tovariObjPrilavok.getNaimenovanie());
//        Picasso.get()
//                .load(tovariObjPrilavok.getFoto_url())
//                .into(holder.foto_url);
//        holder.frameLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TovariObjPrilavok tovariObjPrilavok1 = tovariObjPrilavoks.get(position);
//
//            }
//        });
//        if(tovariObjPrilavok.isSelected()) {
//            holder.tvKKorzine.setVisibility(View.VISIBLE);
//            holder.ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
//        }
//
//        holder.tvKKorzine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent korzinaIntent = new Intent(v.getContext(), Korzina.class);
//                v.getContext().startActivity(korzinaIntent);
//
//            }
//        });
//        holder.ibKupit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!tovariObjPrilavok.isSelected()) {
//                    holder.tvKKorzine.setVisibility(View.VISIBLE);
//                }
//                if(korzinaCount1==0){
//                    tvKorzinaCount1.setVisibility(View.VISIBLE);
//                }
//                holder.ibKupit.setBackgroundResource(R.drawable.backgrbelizakruglzeleniy);
//
//                korzinaCount1++;
//                tvKorzinaCount1.setText(String.valueOf(korzinaCount1));
//                buySQL(tovariObjPrilavok.getId(), userId1 );
//
//            }
//        });
//    }
//
//    @Override
//
//    //return the size of the listItems (developersList)
//
//    public int getItemCount() {
//        return tovariObjPrilavoks.size();
//    }
//
//    void buySQL (final String tovar, final  String pokupatel){
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_TOVAR,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> parameters = new HashMap<String,String>();
//
//                parameters.put("tovar", tovar);
//                parameters.put("pokupatel", pokupatel);
//                return parameters;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//        requestQueue.add(stringRequest);
//    }
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder  {
//
//        // define the View objects
//
//        public TextView tvSkidka;
//        public TextView tvNaimenovanie;
//        public TextView tvCenaBezSkidki;
//        public TextView tvCenaSoSkidkoy;
//        public TextView tvKKorzine;
//        public ImageView foto_url;
//        public ImageButton ibKupit;
//        public FrameLayout frameLay;
//        public ConstraintLayout conlayCenaBezSkidki;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            // initialize the View objects
//
//            tvSkidka = (TextView) itemView.findViewById(R.id.tvSkidkaTov);
//            tvNaimenovanie = (TextView) itemView.findViewById(R.id.tvNaimenovanie);
//            tvCenaBezSkidki = (TextView) itemView.findViewById(R.id.tvCenaBezSkidki);
//            tvCenaSoSkidkoy = (TextView) itemView.findViewById(R.id.tvCenaSoSkidkoy);
//            tvKKorzine = (TextView) itemView.findViewById(R.id.tvKKorzine);
//            foto_url = (ImageView) itemView.findViewById(R.id.ivTovar);
//            ibKupit = (ImageButton) itemView.findViewById(R.id.ibKupit);
//
//            frameLay = (FrameLayout) itemView.findViewById(R.id.frameLay);
//            conlayCenaBezSkidki = (ConstraintLayout) itemView.findViewById(R.id.conlayCenaBezSkidki);
//        }
//
//    }
//}
