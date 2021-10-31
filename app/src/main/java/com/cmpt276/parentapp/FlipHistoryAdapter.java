package com.cmpt276.parentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.model.Coin;

import java.util.ArrayList;


public class FlipHistoryAdapter extends RecyclerView.Adapter<FlipHistoryAdapter.ViewHolder> {

	//taken more or less from android's recyclerview guide
	//https://developer.android.com/guide/topics/ui/layout/recyclerview

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView textViewInfo;
		private final TextView textViewResultTEMP;

		public ViewHolder(View view) {
			super(view);
			textViewInfo = view.findViewById(R.id.textViewFlipInfo);
			textViewResultTEMP = view.findViewById(R.id.textViewResultTemp);
		}

		public TextView getTextViewInfo() {
			return textViewInfo;
		}

		public TextView getTextViewResult() {
			return textViewResultTEMP;
		}
	}

	private Context context;
	private ArrayList<Coin> flips;

	public FlipHistoryAdapter(Context context, ArrayList<Coin> flips){
		this.context = context;
		this.flips = flips;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.flip_history_list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Coin coin = flips.get(position);
		String flipResult;
		switch (coin.getFlipResult()){
			case Coin.HEADS:
				flipResult = context.getString(R.string.heads);
				break;
			case Coin.TAILS:
				flipResult = context.getString(R.string.tails);
				break;
			default:
				throw new IllegalStateException("Cannot have coin flip that has result neither heads nor tails.");
		}

		String out = context.getString(R.string.coin_flip_format, flipResult, coin.getChild().getName(), coin.getTime());
		holder.getTextViewInfo().setText(out);

		if (coin.getFlipChoice() == coin.getFlipResult()){
			holder.getTextViewResult().setText("WON");
		}
		else {
			holder.getTextViewResult().setText("LOST");
		}
	}

	@Override
	public int getItemCount() {
		return flips.size();
	}
}
