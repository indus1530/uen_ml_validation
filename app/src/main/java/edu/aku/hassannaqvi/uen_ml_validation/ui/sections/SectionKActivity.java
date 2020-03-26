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
import edu.aku.hassannaqvi.uen_ml_validation.databinding.ActivitySectionKBinding;
import edu.aku.hassannaqvi.uen_ml_validation.utils.Util;

public class SectionKActivity extends AppCompatActivity {

    ActivitySectionKBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section_k);
        bi.setCallback(this);

        setlistener();

    }

    private void setlistener() {

        /*bi.k101.setOnCheckedChangeListener(((radioGroup, i) -> {

            if (i == bi.k101b.getId()) {
                Clear.clearAllFields(bi.fldGrpCVk101aa);
            }
        }));*/


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
                startActivity(new Intent(this, SectionLActivity.class));
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
        int updcount = db.updatesKishMWRAColumn(KishMWRAContract.SingleKishMWRA.COLUMN_SK, MainApp.kish.getsK());
        if (updcount == 1) {
            return true;
        } else {
            Toast.makeText(this, "Updating Database... ERROR!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void SaveDraft() throws JSONException {

        JSONObject json = new JSONObject();

        json.put("k102", bi.k102a.isChecked() ? "1" :
                bi.k102b.isChecked() ? "2" : "0");

        json.put("l114",
                bi.l114a.isChecked() ? "1" :
                        bi.l114b.isChecked() ? "2" :
                                bi.l114c.isChecked() ? "3" :
                                        bi.l11496.isChecked() ? "96" :
                                                "0");
        json.put("l11496x", bi.l11496x.getText().toString());

        json.put("l107",
                bi.l107a.isChecked() ? "1" :
                        bi.l107b.isChecked() ? "2" :
                                bi.l107c.isChecked() ? "3" :
                                        bi.l107d.isChecked() ? "4" :
                                                bi.l10796.isChecked() ? "96" :
                                                        "0");
        json.put("l10796x", bi.l10796x.getText().toString());

        MainApp.kish.setsK(String.valueOf(json));

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.fldGrpSectionk01);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't go back", Toast.LENGTH_SHORT).show();
    }
}
