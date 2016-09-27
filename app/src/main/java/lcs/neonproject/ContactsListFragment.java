package lcs.neonproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.ViewAsserts;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import lcs.neonproject.contacts.ContactsAdapter;
import lcs.neonproject.utils.LineDivider;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsListFragment extends Fragment {


    public ContactsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolBarSendMoney);
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
        recyclerView.setAdapter(new ContactsAdapter(getActivity(), false));
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
