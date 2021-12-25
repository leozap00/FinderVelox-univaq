package it.univaq.findervelox.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.univaq.findervelox.R;
import it.univaq.findervelox.model.Autovelox;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private final List<Autovelox> data;

    public Adapter(List<Autovelox> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_autovelox, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView subtitle;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.textTitle);
            subtitle = view.findViewById(R.id.textSubtitle);

            view.findViewById(R.id.layoutRoot).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Autovelox autovelox = data.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable("autovelox", autovelox);
            Navigation.findNavController(view).navigate(R.id.action_navList_to_detailActivity, bundle);
        }

        // TODO: 23/12/2021 Controllare il format di onBind 
        public void onBind(Autovelox autovelox) {

            title.setText(autovelox.getLongitude() + " " + autovelox.getLatitude());
            String addr = String.format("%s %s %s dd/MM/yyy at HH:mm:ss",
                    autovelox.getCity(),
                    autovelox.getProvince(),
                    autovelox.getRegion(),
                    autovelox.getDateTimeInsertion());
            subtitle.setText(addr);
        }


    }
}
