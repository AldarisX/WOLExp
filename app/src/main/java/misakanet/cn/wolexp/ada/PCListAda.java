package misakanet.cn.wolexp.ada;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import misakanet.cn.wolexp.R;
import misakanet.cn.wolexp.bean.PC;

public class PCListAda extends BaseAdapter {
    private ArrayList<PC> pcList;
    private LayoutInflater inflater;

    public PCListAda(ArrayList<PC> pcList, LayoutInflater inflater) {
        this.pcList = pcList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return pcList.size();
    }

    @Override
    public Object getItem(int position) {
        return pcList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPC holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pc_item, null);
            holder = new ViewHolderPC();
            holder.pc_item_host = convertView.findViewById(R.id.pc_item_host);
            holder.pc_item_mac = convertView.findViewById(R.id.pc_item_mac);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderPC) convertView.getTag();
        }
        PC pc = pcList.get(position);

        holder.pc_item_host.setText(pc.getHost());
        holder.pc_item_mac.setText(pc.getMac());

        return convertView;
    }

    class ViewHolderPC {
        TextView pc_item_host;
        TextView pc_item_mac;
    }
}
