package misakanet.cn.wolexp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddPCActivity extends AppCompatActivity {

    private EditText et_host;
    private EditText et_mac;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pc);

        init();
    }

    private void init() {
        sp = getSharedPreferences("pcList", Context.MODE_PRIVATE);

        et_host = findViewById(R.id.pc_host);
        et_mac = findViewById(R.id.pc_mac);
    }

    public void btnAddPC(View v) throws JSONException {
        String pcHost = et_host.getText().toString();
        String pcMac = et_mac.getText().toString();
        pcMac = pcMac.replaceAll(":", "");

        if (pcHost.replaceAll(" ", "").length() > 0 && pcMac.length() == 12) {
            pcMac = pcMac.toUpperCase();

            JSONArray pcListJson = new JSONArray(sp.getString("sPCList", "[]"));
            JSONObject pcJson = new JSONObject();
            pcJson.accumulate("host", pcHost);
            pcJson.accumulate("mac", pcMac);
            pcListJson.put(pcJson);

            sp.edit().putString("sPCList", pcListJson.toString()).apply();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this.getApplicationContext(), "数据不正确", Toast.LENGTH_SHORT).show();
        }
    }
}

