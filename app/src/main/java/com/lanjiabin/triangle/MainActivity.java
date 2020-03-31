package com.lanjiabin.triangle;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText mEdit;
    private Button mButton;
    private Triangle mTriangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       initView();
       OnClick();
    }
    public void initView(){
        setContentView(R.layout.activity_main);
        mTriangle=findViewById(R.id.triangle);
        mEdit=findViewById(R.id.edit);
        mButton=findViewById(R.id.button);
    }
    public void OnClick(){
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTriangle.setPercent(Double.parseDouble(mEdit.getText().toString()));
            }
        });
    }
}
