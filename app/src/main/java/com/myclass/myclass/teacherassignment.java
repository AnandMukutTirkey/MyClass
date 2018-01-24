package com.myclass.myclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import static android.app.Activity.RESULT_OK;

/**
 * Created by anand on 17/01/18.
 */

public class teacherassignment extends Fragment {

    Button uploadBtn;
    final static int PICK_PDF_CODE = 2342;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String usersubject;
    String userTeacher;
    String resouceUrl;
    ListView listView;
    String filename;
    ArrayList<String> uploads = new ArrayList<>();
//    ArrayList<String> uploaddates = new ArrayList<>();
//    ArrayList<String> uploadnames = new ArrayList<>();
//    Vector<String> file[];
    Map<String, String> map = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teacherassignment, container, false);

        ViewResources resources = new ViewResources(getContext(),R.layout.teacherassignment,uploads);

        uploadBtn = (Button) rootView.findViewById(R.id.uploadBtn);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        listView = (ListView)rootView.findViewById(R.id.uploads);
        listView.setAdapter(resources);

        final String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        databaseReference.child("resources").child(useremail.replace(".","").replace("@","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploads.clear();
                Log.d("VIMAL","VIMAL");
                Iterable<DataSnapshot> resources = dataSnapshot.getChildren();
                for (DataSnapshot resource : resources){
                    Map<String,Object> map = (HashMap<String,Object>)resource.getValue();
                    uploads.add(map.get("date").toString());
                    uploads.add(map.get("filename").toString());
                    uploads.add(map.get("url").toString());
                    uploads.add(map.get("uploader").toString());

                }
                Log.d("invalidate","invalidate");
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                map.put("uploader", useremail);
                String date = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).format(new Date());





//                FirebaseDatabase.getInstance().getReference().child("users").child(useremail.replace("@","").replace(".","")).child("subject")
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                usersubject = dataSnapshot.getValue(String.class);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                FirebaseDatabase.getInstance().getReference().child("class").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Iterable<DataSnapshot> teachers = dataSnapshot.getChildren();
//                        for (DataSnapshot teacher : teachers) {
//                            Map<String, Object> map = new HashMap<>();
//                            map = (Map<String, Object>) teacher.getValue();
//                            if (map.containsKey(useremail.replace("@", "").replace(".", ""))) {
//                                userTeacher = teacher.getKey();
//                                //upload
//                            }
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF_CODE);
            }
        });
        return rootView;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            progressDialog.setMessage("Uploading ...");
            progressDialog.show();
            Uri uri = data.getData();
            filename = getFileName(uri);
            //pick.setImageURI(uri);
            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").replace("@", "");
            Toast.makeText(getContext(),filename,Toast.LENGTH_LONG).show();
            final StorageReference filepath = storageReference.child("resources").child(filename);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Resource Uploded", Toast.LENGTH_LONG).show();
                    resouceUrl = taskSnapshot.getDownloadUrl().toString();
                    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    map.put("date", date);
                    map.put("uploader", email);
                    map.put("url",resouceUrl);
                    map.put("filename",filename);

                    FirebaseDatabase.getInstance().getReference().child("resources").child(email).push().setValue(map);
                    uploads.clear();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "uploding failed", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "else", Toast.LENGTH_LONG).show();
        }
    }



}

class ViewResources extends ArrayAdapter<String> {
    ArrayList<String> events = new ArrayList<>();
    public ViewResources(Context context, int textViewResourceId, ArrayList<String> objects){
        super(context,textViewResourceId, objects);
        events = objects;
    }


    @Override
    public int getCount() {
        return events.size()/4;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.showresources,null);

        TextView uploader = (TextView)convertView.findViewById(R.id.uploader);
        TextView date = (TextView)convertView.findViewById(R.id.date);
        TextView filename = (TextView)convertView.findViewById(R.id.filename);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(events.get(position*4+2)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });

        uploader.setText(events.get(position*4+3));
        date.setText(events.get(position*4));
        filename.setText(events.get(position*4+1));

        return convertView;
    }
}

