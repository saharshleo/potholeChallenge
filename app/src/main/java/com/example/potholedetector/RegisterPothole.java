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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class RegisterPothole extends AppCompatActivity {
    Button buttonRegister;
    Button buttonConfirm;
    ImageView imageView;
    Intent intent;
    public static final int RequestPermissionCode = 1;
    StorageReference firebaseStorage;
    Bitmap bitmap;
    FirebaseAuth firebaseAuth;
    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_pothole);

        buttonRegister = (Button) findViewById(R.id.btn_register_pothole);
        imageView = (ImageView) findViewById(R.id.clicked_image);
        buttonConfirm = (Button)findViewById(R.id.btn_confirm_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference(firebaseAuth.getCurrentUser().getEmail());
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
                confirmImage(file);
            }
        });
    }

    private void confirmImage(final Uri file) {
        final String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] array = baos.toByteArray();
        final RequestQueue queue = Volley.newRequestQueue(this);
        final StorageReference upload = firebaseStorage.child(firebaseAuth.getCurrentUser().getEmail());
        final UploadTask uploadTask = (UploadTask) firebaseStorage.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterPothole.this,"Sucessfully registered",Toast.LENGTH_SHORT).show();
//                upload.child(firebaseAuth.getCurrentUser().getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                   // @Override
//                   // public void onSuccess(final Uri uri) {
//                        // Got the download URL for 'users/me/profile.png'
//                        Log.d("Uri",file.toString());
//                        StringRequest sr = new StringRequest(Request.Method.GET,"https://api.github.com/users", new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Toast.makeText(RegisterPothole.this,response,Toast.LENGTH_LONG).show();
//                                /*try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    final String val = jsonObject.getString("res_pothole");
//                                    if(val=="1"){
//                                        startActivity(new Intent(RegisterPothole.this,ShowMap.class));
//                                    }else{
//                                        Toast.makeText(RegisterPothole.this,"NOT A POTHOLE!!",Toast.LENGTH_LONG).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } */
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(RegisterPothole.this,error.toString(),Toast.LENGTH_LONG).show();
//                            }
//                        }){
//                            @Override
//                            protected Map<String,String> getParams(){
//                                Map<String,String> params = new HashMap<String, String>();
//                                params.put("uri",file.toString());
//                                return params;
//                            }
//
//                            @Override
//                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                Map<String,String> params = new HashMap<String, String>();
//                                params.put("Content-Type","application/x-www-form-urlencoded");
//                                return params;
//                            }
//                        };
//                        queue.add(sr);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors
//                        Toast.makeText(RegisterPothole.this,"hey",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegisterPothole.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });  /* http://bcf44683.ngrok.io/predict/ */

                /*StringRequest sr = new StringRequest(Request.Method.POST,"http://bcf44683.ngrok.io/predict/", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(RegisterPothole.this,response,Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    final String val = jsonObject.getString("res_pothole");
                                    if(val=="1"){
                                        startActivity(new Intent(RegisterPothole.this,ShowMap.class));
                                    }else{
                                        Toast.makeText(RegisterPothole.this,"NOT A POTHOLE!!",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterPothole.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        })
                        {
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("url","https://ichef.bbci.co.uk/news/410/cpsprodpb/B5F1/production/_106177564_pothole.jpg");
                                try {
                                    return super.getParams();
                                } catch (AuthFailureError authFailureError) {
                                    authFailureError.printStackTrace();
                                    return params;
                                }
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type","application/x-www-form-urlencoded");
                                return params;
                            }
                        };
                        queue.add(sr); */

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterPothole.this);
                    String URL = "https://bcf44683.ngrok.io/predict/";
                    final JSONObject jsonBody = new JSONObject();
                    jsonBody.put("url", "https://ichef.bbci.co.uk/news/410/cpsprodpb/B5F1/production/_106177564_pothole.jpg");
                    final String requestBody = jsonBody.toString();

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("Reached 1","heyya 1");
                                Log.d("Reached 2","heyya 2");
                                String ans = response.getString("is_pothole");
                                Log.d("Reached 3","heyya 3");
                                Toast.makeText(RegisterPothole.this,ans,Toast.LENGTH_LONG).show();
                                if(ans=="1"){
                                    startActivity(new Intent(getApplicationContext(),ShowMap.class));
                                }else{
                                    Toast.makeText(RegisterPothole.this,"Error",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(RegisterPothole.this,"FUCK YOUUU",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) ;/* {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success((response.statusCode), HttpHeaderParser.parseCacheHeaders(response));

                        }
                    }; */

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(RegisterPothole.this,"hey",Toast.LENGTH_SHORT).show();
                    }
                });

    }


            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

                if (requestCode == 69 && resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    file  = data.getData();
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterPothole.this, Manifest.permission.CAMERA)) {

            Toast.makeText(RegisterPothole.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(RegisterPothole.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RegisterPothole.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(RegisterPothole.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
