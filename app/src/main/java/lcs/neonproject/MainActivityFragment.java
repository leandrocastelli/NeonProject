package lcs.neonproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lcs.neonproject.contacts.ContactsManager;
import lcs.neonproject.utils.Constants;
import lcs.neonproject.utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {

    }
    Button btnSendMoney;
    Button btnHistory;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.READ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContactsManager.getInstance(getActivity()).searchForContact(0);
                    btnHistory.setEnabled(true);
                    btnSendMoney.setEnabled(true);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //Adding name and email programmatically
        TextView userName = (TextView) view.findViewById(R.id.txtName);
        userName.setText(getString(R.string.user_name));
        TextView userEmail = (TextView) view.findViewById(R.id.txtEmail);
        userEmail.setText(getString(R.string.email));
        //Just Download token
        Utils.getToken(getString(R.string.user_name), getString(R.string.email), getActivity());

        btnSendMoney = (Button)view.findViewById(R.id.btnSendMoney);
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                        .replace(R.id.fragment_holder,new ContactsListFragment())
                        .addToBackStack("GoBackHome")
                        .commit();
            }
        });
        btnHistory = (Button)view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                        .replace(R.id.fragment_holder,new HistoryFragment())

                        .addToBackStack("GoBackHome")
                        .commit();
            }
        });
        btnHistory.setEnabled(false);
        btnSendMoney.setEnabled(false);
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPermission = getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (checkReadPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        Constants.READ_PERMISSION);
            } else {
                ContactsManager.getInstance(getActivity()).searchForContact(0);
                btnHistory.setEnabled(true);
                btnSendMoney.setEnabled(true);
            }

        } else {
            ContactsManager.getInstance(getActivity()).searchForContact(0);
            btnHistory.setEnabled(true);
            btnSendMoney.setEnabled(true);
        }


        return view;
    }
}
