package com.example.potholedetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterPothole extends AppCompatActivity {
    Button buttonRegister;
    Button buttonConfirm,buttonFeedBack;
    ImageView imageView;
    Intent intent;
    public static final int RequestPermissionCode = 1;
    StorageReference firebaseStorage;
    Bitmap bitmap;
    FirebaseAuth firebaseAuth;
    public Uri file;
    public Uri f_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_pothole);

        buttonRegister = (Button) findViewById(R.id.btn_register_pothole);
        imageView = (ImageView) findViewById(R.id.clicked_image);
        buttonConfirm = (Button)findViewById(R.id.btn_confirm_register);
        buttonFeedBack = (Button)findViewById(R.id.btn_feedback);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        //EnableRuntimePermission();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 69);

            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmImage();
            }
        });

        buttonFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FeedBackForm.class));
            }
        });
    }

    private void confirmImage() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final StorageReference upload = firebaseStorage.child(firebaseAuth.getCurrentUser().getEmail());
        final UploadTask uploadTask;
        /*uploadTask = (UploadTask) firebaseStorage.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        } */


        uploadTask = (UploadTask)firebaseStorage.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("url","https://scx1.b-cdn.net/csz/news/800/2018/potholeshowe.jpg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://bcf44683.ngrok.io/predict/",obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response);
                                try {
                                    //Toast.makeText(getApplicationContext(),response.getString("is_pothole"),Toast.LENGTH_LONG).show();
                                    if(response.getInt("is_pothole") == 1){
                                       // startActivity(new Intent(RegisterPothole.this,ShowMaps.class));
                                        Toast.makeText(getApplicationContext(),"It is a pothole",Toast.LENGTH_LONG).show();
                                    }else if(response.getInt("is_pothole") == 0){
                                        Toast.makeText(getApplicationContext(),"Not a pothole",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),response.getString("is_pothole"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                queue.add(jsObjRequest);

            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 69 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            file = data.getData();
        }
    }
}