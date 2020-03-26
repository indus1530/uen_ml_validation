package edu.aku.hassannaqvi.uen_ml_validation.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.uen_ml_validation.R;
import edu.aku.hassannaqvi.uen_ml_validation.contracts.KishMWRAContract;
import edu.aku.hassannaqvi.uen_ml_validation.core.DatabaseHelper;
import edu.aku.hassannaqvi.uen_ml_validation.core.MainApp;
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionH2Binding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionH2Activity extends AppCompatActivity {

    ActivitySectionH2Binding bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_h2);
        bi.setCallback(this);
        setupSkips();


    }


    private void setupSkips() {

        /*if (MainApp.G102.equals("1")) {
            Clear.clearAllFields(bi.fldGrpCVh201);
            bi.fldGrpCVh201.setVisibility(View.GONE);
        } else {
            bi.fldGrpCVh201.setVisibility(View.VISIBLE);
        }*/


    }

    public void BtnContinue() {
        if (formValidation()) {
            try {
                SaveDraft();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (UpdateDB()) {
                finish();
                startActivity(new Intent(this, SectionKActivity.class));
            } else {
                Toast.makeText(this, "Failed to Update Database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void BtnEnd() {
        Util.openEndActivity(this);
    }

    private boolean UpdateDB() {

        DatabaseHelper db = MainApp.appInfo.getDbHelper();
        int updcount = db.updatesKishMWRAColumn(KishMWRAContract.SingleKishMWRA.COLUMN_SH2, MainApp.kish.getsH2());
        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();



        MainApp.kish.setsH2(String.valueOf(json));

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }

}
