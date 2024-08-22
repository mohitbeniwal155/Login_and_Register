package com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.assessment.ForgetPasswordActivity;
import com.example.assessment.MainActivity;
import com.example.assessment.R;
import com.example.assessment.RegisterActivity;
import com.example.assessment.SharedPreference;
import com.example.assessment.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button login,register;
    private EditText email,password;
    String eml,pswd;
    ProgressBar progressBar;
    UtilService utilService;
    SharedPreference sharedPreference;
    TextView forget_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        progressBar=findViewById(R.id.pgbar);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forget_btn=findViewById(R.id.forgotPasswordLink);

        utilService= new UtilService();
        sharedPreference = new SharedPreference(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, LoginActivity.this);

                eml=email.getText().toString();
                pswd=password.getText().toString();

                if(validate(v)){
                    loginUser(v);

                }
            }


        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ireg=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(ireg);
            }
        });

        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });
    }
    public boolean validate(View view) {
        boolean isvalid;

        if (!TextUtils.isEmpty(eml)) {
            if (!TextUtils.isEmpty(pswd)) {
                isvalid = true;
            } else {
                utilService.showSnackBar(view, "Please enter password...");
                isvalid = false;
            }
        } else {
            utilService.showSnackBar(view, "Please enter email...");
            isvalid = false;
        }
        return isvalid;
    }
    private void loginUser(View v) {
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();

        params.put("email",eml);
        params.put("password",pswd);

        String apiKey="https://backend-dumy.onrender.com/api/user/login";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, apiKey,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {

//                        JSONObject dataObject = response.getJSONObject("data");

                        Log.d("RegisterResponse", response.toString(1));

                        String token = response.optString("token");

                        sharedPreference.setValue_string("token",token);
//

//                        Toast.makeText(Login.this, token, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace(); // Log the error
                    progressBar.setVisibility(View.GONE);
                    utilService.showSnackBar(v, "Error parsing response");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(error instanceof ServerError && response !=null){
                    try {
                        String res =new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));

                        JSONObject obj=new JSONObject(res);
                        Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }catch ( JSONException je) {
                        je.printStackTrace();
                        progressBar.setVisibility(View.GONE);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");


                return params;
            }
        };

        int socketTimeout=4000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
        if(pref.contains("token")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }
}