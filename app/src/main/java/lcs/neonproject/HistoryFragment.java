package lcs.neonproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import lcs.neonproject.contacts.ContactsAdapter;
import lcs.neonproject.utils.LineDivider;

/**
 * Created by Leandro on 9/25/2016.
 */

public class HistoryFragment extends Fragment{
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolBarSendMoney);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.history));
        toolbar.setTitle("");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LineDivider(getContext()));
        recyclerView.setAdapter(new ContactsAdapter(getActivity(),true));
        return view;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onStop() {
        if (ContactsAdapter.progressDialog != null && ContactsAdapter.progressDialog.isShowing()) {
            ContactsAdapter.progressDialog.dismiss();
        }
        super.onStop();
    }
}
