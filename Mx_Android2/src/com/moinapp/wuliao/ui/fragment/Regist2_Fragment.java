package com.moinapp.wuliao.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.moinapp.wuliao.activity.Fragment_skip;
import com.moinapp.wuliao.R;

public class Regist2_Fragment extends Base_Fragment {
	
	private Fragment_skip callback;
	private TextView regist_submit;
	private EditText nickname_et;
	private ImageView nickname_ok;
	private RadioGroup gender_rg;
	private RadioButton rb_male, rb_female, rb_unknow;
	private String nickname_str, gender_str;
	
	private boolean nickname_state = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.regist2_layout, container, false);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (Fragment_skip) activity;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	private void initView() {
		regist_submit = (TextView) getActivity().findViewById(R.id.regist_submit);
		nickname_et = (EditText) getActivity().findViewById(R.id.nickname);
		nickname_ok = (ImageView) getActivity().findViewById(R.id.nickname_ok);
		gender_rg = (RadioGroup) getActivity().findViewById(R.id.regist_gender);
		rb_male = (RadioButton) getActivity().findViewById(R.id.gender_male);
		rb_female = (RadioButton) getActivity().findViewById(R.id.gender_female);
		rb_unknow = (RadioButton) getActivity().findViewById(R.id.gender_unknow);
		
		nickname_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				nickname_str = arg0.toString();
				if (nickname_str.length() == 0) {
					nickname_state = false;
					nickname_ok.setVisibility(View.INVISIBLE);
				} else {
					nickname_state = true;
					nickname_ok.setVisibility(View.VISIBLE);
				}
			}
		});
		
		gender_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedid) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(nickname_et.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
				if (checkedid == rb_male.getId()) {
					gender_str = rb_male.getText().toString();
				} else if (checkedid == rb_female.getId()) {
					gender_str = rb_female.getText().toString();
				} else {
					gender_str = rb_unknow.getText().toString();
				}
			}
		});
		
		regist_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (nickname_state) {
					callback.skip(2, nickname_str, gender_str);
				} else {
					Toast.makeText(getActivity(), R.string.nickname_null, Toast.LENGTH_SHORT).show();
					nickname_et.requestFocus();
				}
			}
		});
	}
}
