package ordo.azurewebsites.net.ordo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ordo.azurewebsites.net.ordo.model.ItemOrder;
import ordo.azurewebsites.net.ordo.model.ItemOrderLib;

public class HistoryFragment extends Fragment {
    private RecyclerView mItemOrderRecyclerView;
    private ItemOrderAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        mItemOrderRecyclerView = (RecyclerView) view.findViewById(R.id.item_order_recycler_view);

        mItemOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ItemOrderLib itemOrderLib = ItemOrderLib.get(getActivity());
        List<ItemOrder> itemOrders =  itemOrderLib.getItemOrders();
        if (mAdapter == null) {
            mAdapter = new ItemOrderAdapter(itemOrders);
            mItemOrderRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ItemOrderHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ItemOrder mItemOrder;

        public void bind(ItemOrder itemOrder){
            mItemOrder = itemOrder;
            mTitleTextView.setText(itemOrder.getTitle());
            mDateTextView.setText(itemOrder.getDate().toString());
        }

        public ItemOrderHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.row_item_item_order,parent,false));
            mTitleTextView = itemView.findViewById(R.id.item_order_title);
            mDateTextView = itemView.findViewById(R.id.item_order_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = ItemOrderActivity.newIntent(getActivity(),mItemOrder.getId());
            startActivity(intent);
        }
    }

    private class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderHolder> {
        private List<ItemOrder> mItemOrders;

        public ItemOrderAdapter(List<ItemOrder> itemOrders){
            mItemOrders = itemOrders;
        }

        @NonNull
        @Override
        public ItemOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemOrderHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemOrderHolder itemOrderHolder, int i) {
            ItemOrder itemOrder = mItemOrders.get(i);
            itemOrderHolder.bind(itemOrder);
        }

        @Override
        public int getItemCount() {
            return mItemOrders.size();
        }
    }
}
