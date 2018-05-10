package misakanet.cn.wolexp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import misakanet.cn.wolexp.ada.PCListAda;
import misakanet.cn.wolexp.bean.PC;
import misakanet.cn.wolexp.tool.WOLHelper;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_PC = 7637;
    private ListView lvPCList;
    private ArrayList<PC> pcList = new ArrayList<>();
    private PCListAda pcListAda;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        sp = getSharedPreferences("pcList", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvPCList = findViewById(R.id.lv_pcList);
        pcListAda = new PCListAda(pcList, getLayoutInflater());
        lvPCList.setAdapter(pcListAda);
        lvPCList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout pcItem = (LinearLayout) view;
                TextView pcHost = pcItem.findViewById(R.id.pc_item_host);
                final TextView pcMac = pcItem.findViewById(R.id.pc_item_mac);
                final TextView pcPort = pcItem.findViewById(R.id.pc_item_port);
                final TextView pcAddr = pcItem.findViewById(R.id.pc_item_addr);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("给" + pcHost.getText().toString() + "发送开机命令")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WOLHelper.sendWOL(pcMac.getText().toString(), Integer.parseInt(pcPort.getText().toString()), pcAddr.getText().toString());
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        lvPCList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LinearLayout pcItem = (LinearLayout) view;
                TextView pcHost = pcItem.findViewById(R.id.pc_item_host);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除" + pcHost.getText().toString())
                        .setMessage("确定要删除么")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    JSONArray pcListJson = new JSONArray(sp.getString("sPCList", "[]"));
                                    pcListJson.remove(position);
                                    sp.edit().putString("sPCList", pcListJson.toString()).apply();
                                    loadPCList();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddPCActivity.class); //设置跳转的Activity
                startActivityForResult(intent, ADD_PC);
            }
        });

        FloatingActionButton fabSendAll = findViewById(R.id.fab_sendAll);
        fabSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("确定要全部唤醒么")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONArray pcListJson = null;
                                try {
                                    pcListJson = new JSONArray(sp.getString("sPCList", "[]"));
                                    for (int i = 0; i < pcListJson.length(); i++) {
                                        JSONObject pcJson = pcListJson.getJSONObject(i);
                                        WOLHelper.sendWOL(pcJson.getString("mac"), pcJson.getInt("port"), pcJson.getString("addr"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        try {
            loadPCList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PC) {
            try {
                loadPCList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPCList() throws JSONException {
        pcList.clear();
        JSONArray pcListJson = new JSONArray(sp.getString("sPCList", "[]"));
        for (int i = 0; i < pcListJson.length(); i++) {
            JSONObject pcJson = pcListJson.getJSONObject(i);
            PC pc = new PC();
            pc.setHost(pcJson.getString("host"));
            pc.setMac(pcJson.getString("mac"));
            pc.setPort(pcJson.getInt("port"));
            pc.setAddr(pcJson.getString("addr"));
            pcList.add(pc);
        }
        pcListAda.notifyDataSetChanged();
    }


}
