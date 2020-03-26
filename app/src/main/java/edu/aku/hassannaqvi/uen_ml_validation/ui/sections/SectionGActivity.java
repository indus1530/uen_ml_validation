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
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionGBinding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionGActivity extends AppCompatActivity {

    ActivitySectionGBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_g);
        bi.setCallback(this);

        setupSkips();

    }

    private void setupSkips() {

        /*//g102
        bi.g102.setOnCheckedChangeListener((group, checkedId) -> {
            Clear.clearAllFields(bi.fldGrpCVg103);
            Clear.clearAllFields(bi.fldGrpCVg105);
            Clear.clearAllFields(bi.fldGrpCVg106);
            Clear.clearAllFields(bi.fldGrpCVg107);
            bi.fldGrpCVg103.setVisibility(View.GONE);
            bi.fldGrpCVg105.setVisibility(View.GONE);
            bi.fldGrpCVg106.setVisibility(View.GONE);
            bi.fldGrpCVg107.setVisibility(View.GONE);

            if (checkedId == bi.g102a.getId()) {
                bi.fldGrpCVg103.setVisibility(View.VISIBLE);
            } else {
                bi.fldGrpCVg105.setVisibility(View.VISIBLE);
                bi.fldGrpCVg106.setVisibility(View.VISIBLE);
                bi.fldGrpCVg107.setVisibility(View.VISIBLE);
            }
        });*/

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
                startActivity(new Intent(this, SectionH1Activity.class));
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
        int updcount = db.updatesKishMWRAColumn(KishMWRAContract.SingleKishMWRA.COLUMN_SG, MainApp.kish.getsG());
        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("g102", bi.g102a.isChecked() ? "1" :
                bi.g102b.isChecked() ? "2" :
                        bi.g102c.isChecked() ? "3" :
                                bi.g102d.isChecked() ? "4" :
                                        bi.g102e.isChecked() ? "5" :
                                                bi.g102f.isChecked() ? "6" : "0");

        json.put("g109", bi.g109a.isChecked() ? "1" :
                bi.g109b.isChecked() ? "2" :
                        bi.g109c.isChecked() ? "3" :
                                bi.g109d.isChecked() ? "4" : "0");

        json.put("g126", bi.g126a.isChecked() ? "1" :
                bi.g126b.isChecked() ? "2" :
                        bi.g12697.isChecked() ? "97" : "0");

        MainApp.kish.setsG(String.valueOf(json));

        MainApp.G102 = bi.g102a.isChecked() ? "1" : "0";

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.fldGrpSectionG);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }


}
