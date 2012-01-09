package com.devspacenine.commonfragmentslibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * A DialogFragment that displays a message with a "yes" or "no" response and returns
 * the decision to the parent activity, which must implement onPositiveDecision and
 * onNegativeDecision methods
 * @author yeluapyeroc
 *
 */
public class SimpleDecisionDialogFragment extends DialogFragment {
	private OnDecisionMadeListener mListener;
	
	public interface OnDecisionMadeListener {
		public void onPositiveDecision(DialogInterface dialog, int requestCode, Parcelable data);
		public void onNegativeDecision(DialogInterface dialog, int requestCode, Parcelable data);
	}
	
	public static SimpleDecisionDialogFragment newInstance(String title, String message,
			int requestCode, Parcelable data, int listenerFragmentId) {
		SimpleDecisionDialogFragment frag = new SimpleDecisionDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putInt("request_code", requestCode);
		args.putParcelable("data", data);
		args.putInt("listener_fragment_id", listenerFragmentId);
		frag.setArguments(args);
		return frag;
	}
	
	public static SimpleDecisionDialogFragment newInstance(String title, String message,
			int requestCode, Parcelable data) {
		SimpleDecisionDialogFragment frag = new SimpleDecisionDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putInt("request_code", requestCode);
		args.putParcelable("data", data);
		frag.setArguments(args);
		return frag;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		FragmentActivity fragActivity = (FragmentActivity) activity;
		
		if(getArguments().containsKey("listener_fragment_id")) {
			Fragment frag = fragActivity.getSupportFragmentManager().findFragmentById(
					getArguments().getInt("listener_fragment_id"));
			try {
				mListener = (OnDecisionMadeListener) frag;
			}catch(ClassCastException e) {
				throw new ClassCastException(frag
						+ " must implement the OnDecisionMadeListener interface.");
			}
		}else{
			try {
				mListener = (OnDecisionMadeListener) activity;
			}catch(ClassCastException e) {
				throw new ClassCastException(activity
						+ " must implement the OnDecisionMadeListener interface.");
			}
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		String message = getArguments().getString("message");
		final Parcelable data = getArguments().getParcelable("data");
		final int request_code = getArguments().getInt("request_code");
		
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener.onPositiveDecision(dialog, request_code, data);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener.onNegativeDecision(dialog, request_code, data);
					}
				})
				.create();
	}
}