package it.univaq.findervelox.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.findervelox.R;
import it.univaq.findervelox.database.DB;
import it.univaq.findervelox.model.Autovelox;
import it.univaq.findervelox.utility.OnRequestListener;
import it.univaq.findervelox.utility.Pref;
import it.univaq.findervelox.utility.Requests;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Autovelox> data = new ArrayList<>();
    private Adapter adapter = new Adapter(data);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        if(Pref.load(requireContext(), "firstStart", true)) {
            download();
        } else {
            load();
        }
    }

    private void load() {
        new Thread(() -> {
            DB.getInstance(requireContext()).autoveloxDao().findAll();
            recyclerView.post(() -> adapter.notifyDataSetChanged());

        }).start();
    }

    private void download() {
        Requests.asyncRequest(new OnRequestListener() {

            private DialogProgress dialog;

            @Override
            public void onRequestCompleted(byte[] data) {
                if(data == null)
                    return;

                List<Autovelox> list = new ArrayList<>();
                // TODO: 23/12/2021 Controllare come splittare le singole parti se json è del tipo {key:value,key:value,...}
                String result = new String(data);
                String[] lines = result.split("\r\n");
                for(int i=0; i< lines.length; i++) {
                    String line = lines[i];
                    String[] parts = line.split(",");

                    Autovelox autovelox = Autovelox.parseData(parts);
                    if(autovelox != null)
                        list.add(autovelox);

                }

                ListFragment.this.data.addAll(list);
                recyclerView.post(() -> {
                    if(dialog != null)
                        dialog.dismiss();
                    adapter.notifyDataSetChanged();
                });

                DB.getInstance(requireContext()).autoveloxDao().save(list);

                Pref.save(requireContext(), "firstStart", false);
            }

            @Override
            public void onRequestUpdate(int progress) {

                if(dialog == null) {
                    dialog = new DialogProgress();
                    dialog.show(getChildFragmentManager(), "dialog-progress");
                }
                dialog.updatePRogress(progress);
            }
        });
    }
}
