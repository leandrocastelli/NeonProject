package lcs.neonproject.contacts;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import lcs.neonproject.MainActivity;
import lcs.neonproject.R;
import lcs.neonproject.model.Contact;
import lcs.neonproject.model.Model;
import lcs.neonproject.model.Transaction;
import lcs.neonproject.net.NetDownloader;
import lcs.neonproject.utils.Constants;
import lcs.neonproject.utils.EditTextWatcher;
import lcs.neonproject.utils.Utils;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Leandro on 9/24/2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Model> mDataSet;
    private boolean isHistory = false;
    public static AdapterHandler myHandler;
    public static ProgressDialog progressDialog;

    class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View v) {
            super(v);
        }
    }

    public ContactsAdapter(Context context, boolean isHistory) {
        this.context = context;
        this.isHistory = isHistory;
        myHandler = new AdapterHandler();
        if (isHistory) {
            NetDownloader.getInstance().getHistory(context);

        } else {
            //ToDo Load in another thread
            //mDataSet = ContactsManager.getInstance(context).getAllContacts();
            Thread myThread = new Thread(new ContactsLoaderThread());
            myThread.start();

        }


        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage(context.getString(R.string.loading));
        if (progressDialog!= null) {
            progressDialog.show();
        }


        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        RecyclerView.ViewHolder vh = new MyHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TextView contactName = (TextView)holder.itemView.findViewById(R.id.contactName);
        TextView contactNumber = (TextView)holder.itemView.findViewById(R.id.contactNumber);
        ViewGroup cardView = (RelativeLayout)holder.itemView.findViewById(R.id.relative_layout_holder);
        CircleImageView circleImageView = (CircleImageView) holder.itemView.findViewById(R.id.contactPicture);

        TextView letter = (TextView)holder.itemView.findViewById(R.id.lettersWorkAround);
        final Contact contact = mDataSet.get(position).getContact();
        contactName.setText(contact.getName());
        contactNumber.setText(contact.getPhoneNumber());
        if (contact.getProfilePicture() != 0) {
            Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                    .parseLong(contact.getId()));

            circleImageView.setImageURI(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
            letter.setText("");
        } else {
            circleImageView.setImageResource(R.drawable.solid_white);

            String[] names = contact.getName().split(" ");
            String temp = null;
            if (names.length > 1) {
                temp = ""+names[0].charAt(0)+names[1].charAt(0);
            } else {
                temp = ""+names[0].charAt(0);
            }
            letter.setText(temp.toUpperCase());
        }
        if (!isHistory) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_layout, null, false);
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(v.getContext())
                                    .setView(dialogView);
                    final Dialog dialog = builder.create();
                    ImageButton closeButton = (ImageButton) dialogView.findViewById(R.id.closeBtn);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final EditText edt = (EditText) dialogView.findViewById(R.id.edtValue);

                    edt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //work around

                            edt.setSelection(edt.getText().length());


                        }
                    });
                    edt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                InputMethodManager inputManager =
                                        (InputMethodManager) context.
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(
                                        edt.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                return true;
                            }
                            return false;
                        }
                    });
                    //Set Name, Number and Photo
                    TextView contactName = (TextView) dialogView.findViewById(R.id.dialogName);
                    TextView contactNumber = (TextView) dialogView.findViewById(R.id.dialogNumber);
                    contactName.setText(contact.getName());
                    contactNumber.setText(contact.getPhoneNumber());
                    TextView letters = (TextView) dialogView.findViewById(R.id.lettersDialog);
                    CircleImageView circleImageView1 = (CircleImageView) dialogView.findViewById(R.id.dialogPicture);

                    if (contact.getProfilePicture() != 0) {
                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                                .parseLong(contact.getId()));

                        circleImageView1.setImageURI(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                        letters.setText("");
                    } else {
                        String[] names = contact.getName().split(" ");
                        String temp = null;
                        if (names.length > 1) {
                            temp = ""+names[0].charAt(0)+names[1].charAt(0);
                        } else {
                            temp = ""+names[0].charAt(0);
                        }
                        letters.setText(temp.toUpperCase());
                    }
                    //Send Button
                    Button sendButton = (Button) dialogView.findViewById(R.id.dialogSend);
                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar.make(holder.itemView, view.getContext().getString(R.string.sending_money), Snackbar.LENGTH_SHORT)
                                    .show();
                            NetDownloader.getInstance().sendMoney(edt.getText().toString(), contact.getId(), holder.itemView.getContext());
                            dialog.dismiss();
                        }
                    });
                    edt.addTextChangedListener(new EditTextWatcher(edt));
                    dialog.show();


                }
            });
        } //History
            else {
            TextView dateText = (TextView)holder.itemView.findViewById(R.id.historyInfo);
            Model temp = mDataSet.get(position);
            Transaction transaction;
            if (temp instanceof Transaction) {
                transaction = (Transaction)temp;
            } else {
                return;
            }
            Date date = Utils.formatDate(transaction.getDate());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            DecimalFormat mFormat = new DecimalFormat("00");
            dateText.setText(calendar.get(Calendar.DAY_OF_MONTH) + " de " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt","BR"))
            + " de " + calendar.get(Calendar.YEAR)+ " - às " + calendar.get(Calendar.HOUR_OF_DAY) +
            "h"+mFormat.format(calendar.get(Calendar.MINUTE)));

        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null?0:mDataSet.size();
    }

    public class AdapterHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_DATE_UPDATE: {

                    Bundle bundle = msg.getData();
                    String jSon = bundle.getString("History");
                    Gson gson = new GsonBuilder().create();

                    Type listTypePost = new TypeToken<ArrayList<Transaction>>() {
                    }.getType();

                    List<Model> postList = gson.fromJson(jSon.toString(), listTypePost);

                    mDataSet = postList;
                    notifyDataSetChanged();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                }
                case Constants.MSG_CONTACTS_UPDATE: {
                    Bundle bundle = msg.getData();
                    mDataSet = (List<Model>) bundle.getSerializable("Contacts");
                    notifyDataSetChanged();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                }
                default: {
                    if (progressDialog!= null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        }
    }
}
