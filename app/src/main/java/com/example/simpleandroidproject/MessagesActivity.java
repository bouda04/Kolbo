package com.example.simpleandroidproject;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kolbodb.Message;
import com.example.kolbodb.MyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class MessagesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getActionBar().show();

        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(new MyAdapter(this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Message msg = (Message) adapterView.getAdapter().getItem(position);
                ResponseDLG dlg = new ResponseDLG(msg);
                dlg.setStyle(DialogFragment.STYLE_NORMAL, 0);
                dlg.setCancelable(false);
                dlg.show(getFragmentManager(), null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignupActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MyAdapter extends ArrayAdapter<Message>{

        public MyAdapter(@NonNull Context context) {
            super(context, android.R.layout.simple_list_item_1);
            listenToNewMessages();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            // Lookup view for data population
            TextView tvDescription = (TextView) convertView.findViewById(android.R.id.text1);
            Message msg = getItem(position);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(msg.getCreationTime());
            SimpleDateFormat format = new SimpleDateFormat("d/mm/yyyy 'at' HH:mm");
            tvDescription.setText(Html.fromHtml("<b>" + format.format(cal.getTime()) + ":" +
                    "</b><br><p><font color='#145A14'>"+msg.getRequest()+"</p></font>"));
            return convertView;
        }

        private void listenToNewMessages(){
            final DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String msgID = dataSnapshot.getKey();
                    Message msg = dataSnapshot.getValue(Message.class);
                    msg.setMsgID(msgID);
                    add(msg);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Message msg = dataSnapshot.getValue(Message.class);
                    remove(msg);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }


    public class ResponseDLG extends DialogFragment
    {
        Message msg;



        public  ResponseDLG(Message msg)
        {
            this.msg   = msg;


        }
        @Override
        public View onCreateView(LayoutInflater l, ViewGroup v, Bundle b)
        {

            final View view = l.inflate(R.layout.manager_dialog, v  ,false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


            TextView tv = (TextView)view.findViewById(R.id.clientMessage);
            tv.setText(msg.getRequest());


            Button send = 	(Button)view.findViewById(R.id.send);
            final EditText edNewPrice = (EditText)view.findViewById(R.id.edtNewPrice);
            edNewPrice.addTextChangedListener(new TextWatcher() {



                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            send.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    String newPrice = String.valueOf(edNewPrice.getText());
                    msg.setResponse(newPrice);
                    sendResponseToClient(msg);
                    dismiss();
                }

            });
            Button cancel = 	(Button)view.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dismiss();
                }

            });

            return view;
        }

        private void sendResponseToClient(Message msg){

            final DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("messages").child(msg.getMsgID()).child("response").setValue(msg.getResponse());
        }
    }
}
