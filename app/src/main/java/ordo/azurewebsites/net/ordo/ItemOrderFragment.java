package ordo.azurewebsites.net.ordo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

import ordo.azurewebsites.net.ordo.model.ItemOrder;
import ordo.azurewebsites.net.ordo.model.ItemOrderLib;

public class ItemOrderFragment extends Fragment {
    private static final String ARG_ITEM_ORDER_ID = "item_order_id";

    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ItemOrder mItemOrder;

    public static ItemOrderFragment newInstance(UUID itemOrderId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ORDER_ID, itemOrderId);
        ItemOrderFragment fragment = new ItemOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemOrderId = (UUID) getArguments().getSerializable(ARG_ITEM_ORDER_ID);
        mItemOrder = ItemOrderLib.get(getActivity()).getItemOrder(itemOrderId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_order,container,false);

        mTitleTextView = v.findViewById(R.id.item_order_title);
        mDateTextView = v.findViewById(R.id.item_order_date);

        mTitleTextView.setText(mItemOrder.getTitle());
        mDateTextView.setText(mItemOrder.getTitle());

        return v;
    }
}
