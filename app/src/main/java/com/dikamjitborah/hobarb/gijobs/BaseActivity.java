package com.dikamjitborah.hobarb.gijobs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    Dialog dialog;
    public void showProgressBar(Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar_dialog);

        ProgressBar progressBar = dialog.findViewById(R.id.progress_pb_dialog);
        dialog.setCancelable(false);
        dialog.show();


    }

    public void hideProgressBar(){
        if(dialog!=null &&dialog.isShowing())
        {
            dialog.dismiss();
            dialog = null;
        }

    }
}
