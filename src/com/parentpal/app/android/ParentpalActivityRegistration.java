/**
 * 
 */
package com.parentpal.app.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parentpal.app.asynctask.RegistrationAsycTask;
import com.parentpal.app.asynctask.ServerResponseCallback;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 * 
 *         Parentpal Oct 03, 2013 2:18:12 PM
 * 
 */
public class ParentpalActivityRegistration extends ParentpalBaseActivity
		implements OnClickListener, ServerResponseCallback{

	private Button btnRegister;
	private Button btnCancel;

	private EditText inputUserCompleteName;
	private EditText inputUserName;
	private EditText inputPassword;
	private EditText inputEmailId;
	private EditText inputMobileNumber;
	
	private String stringCompleteName;
	private String stringUserName;
	private String stringPassword;
	private String stringEmailID;
	private String stringMobileNumber;
	
	
	private Context mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_registration);
		mContext = ParentpalActivityRegistration.this;
		initializeComponent();
	}

	/**
	 * 
	 */
	private void initializeComponent() {
		// TODO Auto-generated method stub

		btnRegister = (Button) findViewById(R.id.btnRegistration);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		
		inputUserCompleteName = (EditText) findViewById(R.id.inputUserCompleteName);
		inputUserName = (EditText) findViewById(R.id.inputUserName);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		inputEmailId = (EditText) findViewById(R.id.inputEmailId);
		inputMobileNumber = (EditText) findViewById(R.id.inputMobileNumber);
		
		
		btnCancel.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		Intent intent;

		switch (view.getId()) {
		case R.id.btnRegistration:

			stringCompleteName = inputUserCompleteName.getText().toString().trim();
			stringUserName = inputUserName.getText().toString().trim();
			stringPassword = inputPassword.getText().toString().trim();
			stringEmailID = inputEmailId.getText().toString().trim();
			stringMobileNumber = inputMobileNumber.getText().toString().trim();
			
			if (verifyInputs(stringCompleteName, stringUserName, stringPassword, stringEmailID, stringMobileNumber)) {
		
				showWait("Registering with ParentPal...");
				
				new RegistrationAsycTask().execute(mContext, stringCompleteName, stringUserName, stringPassword, stringEmailID, stringMobileNumber);
				
			}
			
			break;

		case R.id.btnCancel:
			intent = new Intent(ParentpalActivityRegistration.this,
					ParentpalActivityLogin.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	/**
	 * @param mobileNumber 
	 * @param emailID 
	 * @param password 
	 * @param userName 
	 * @param completeName 
	 * 
	 */
	private boolean verifyInputs(String completeName, String userName, String password, String emailID, String mobileNumber) {
		// TODO Auto-generated method stub
		
		if (TextUtils.isEmpty(completeName)) {
			
			inputUserCompleteName.setError("Please Enter First and Last Name.");
			
			return false;
		}else if (TextUtils.isEmpty(userName)) {
			
			inputUserName.setError("Please Enter User Name.");
			
			return false;
		}else if (userName.length() < 6) {
			
			inputUserName.setError("User name must be larger then 6 character.");
			
			return false;
			
		}else if (TextUtils.isEmpty(password)) {
			
			inputPassword.setError("Please Enter Password.");
			
			return false;
			
		}else if (password.length() < 6) {
			
			inputPassword.setError("Password must be larger then 6 character.");
			
			return false;
			
		}else if (TextUtils.isEmpty(emailID)) {
			
			inputUserName.setError("Please Enter Email ID.");
			
			return false;
			
		}else if (TextUtils.isEmpty(mobileNumber)) {
			
			inputUserName.setError("Please Enter Mobile Number.");
			
			return false;
			
		}else if (mobileNumber.length() < 6 ) {
			
			inputUserName.setError("Please Enter correct mobile number.");
			
			return false;
			
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.parentpal.app.asynctask.ServerResponseCallback#RequestResponseCallBack(boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void RequestResponseCallBack(boolean isSuccess, String responseMsg) {
		// TODO Auto-generated method stub
		
		hideWait();
		
		if (isSuccess) {
			
			Intent intent = new Intent(ParentpalActivityRegistration.this,ParentpalTabActivity.class);
			startActivity(intent);
			finish();
					 
		}else {
			ShowSimpleDialog("Error", responseMsg);
		}
	}
}
