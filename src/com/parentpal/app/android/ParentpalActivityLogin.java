/**
 * 
 */
package com.parentpal.app.android;

import com.parentpal.app.asynctask.LoginAsycTask;
import com.parentpal.app.asynctask.ServerResponseCallback;
import com.parentpal.app.util.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 * 
 *         Parentpal Oct 04, 2013 10:51:09 PM
 * 
 */
public class ParentpalActivityLogin extends ParentpalBaseActivity implements
		OnClickListener, ServerResponseCallback {

	private Button btnLogin;
	private Button btnRegistration;
	
	private EditText inputUserName;
	private EditText inputPassword;
	
	private String stringUsername;
	private String stringPassword;
	
	private CheckBox checkBoxRememberme;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_login);

		initializeComponent();
		
		if (Utility.getBooleanPreference(ParentpalActivityLogin.this, Utility.KEY_IS_REMEMBER)) {
			checkBoxRememberme.setChecked(true);
			inputUserName.setText(Utility.getStringPreference(ParentpalActivityLogin.this, Utility.KEY_UESRNAME));
			inputPassword.setText(Utility.getStringPreference(ParentpalActivityLogin.this, Utility.KEY_PASSWORD));
		}else {
			Utility.setBooleanPreference(ParentpalActivityLogin.this, Utility.KEY_IS_REMEMBER, false);
			Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_UESRNAME, "");
			Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_PASSWORD, "");
		}

	}

	/**
	 * Inflate and Intialize the XML component
	 */
	private void initializeComponent() {
		// TODO Auto-generated method stub
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegistration = (Button) findViewById(R.id.btnRegistration);
		
		inputUserName = (EditText) findViewById(R.id.inputUserName);
		inputPassword = (EditText) findViewById(R.id.inputPassword);

		checkBoxRememberme = (CheckBox) findViewById(R.id.checkBoxRememberMe);
		
		btnLogin.setOnClickListener(this);
		btnRegistration.setOnClickListener(this);
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
		case R.id.btnLogin:

			stringUsername = inputUserName.getText().toString().trim();
			stringPassword = inputPassword.getText().toString().trim();
			
			if (verifyInputs(stringUsername, stringPassword)) {
				
				showWait("Authenticating...");
				
				new LoginAsycTask().execute(ParentpalActivityLogin.this, stringUsername, stringPassword);
				
			}
			
			break;

		case R.id.btnRegistration:
			intent = new Intent(ParentpalActivityLogin.this,
					ParentpalActivityRegistration.class);
			startActivity(intent);
			finish();
			break;
		}

	}

	/**
	 * @param userName
	 * @param password
	 */
	private boolean verifyInputs(String userName, String password) {
		// TODO Auto-generated method stub
	
		if (TextUtils.isEmpty(userName)) {
			inputUserName.setError("Please enter user name.");
			return false;
		}else if (userName.length() < 6) {
			inputUserName.setError("Please enter corrent user name.");
			return false;
		}else if (TextUtils.isEmpty(password)) {
			inputPassword.setError("Please enter password.");
			return false;
		}else if (password.length() < 6) {
			inputPassword.setError("Please enter corrent password.");
			return false;
		}
		
		return true;
		
	}

	/* (non-Javadoc)
	 * @see com.parentpal.app.asynctask.ServerResponseCallback#RequestResponseCallBack(boolean, java.lang.String)
	 */
	@Override
	public void RequestResponseCallBack(boolean isSuccess, String responseMsg) {
		// TODO Auto-generated method stub
	
		hideWait();
		
		if (isSuccess) {
			
			if (checkBoxRememberme.isChecked()) {
				Utility.setBooleanPreference(ParentpalActivityLogin.this, Utility.KEY_IS_REMEMBER, true);
				Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_UESRNAME, stringUsername);
				Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_PASSWORD, stringPassword);
			}else {
				Utility.setBooleanPreference(ParentpalActivityLogin.this, Utility.KEY_IS_REMEMBER, false);
				Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_UESRNAME, "");
				Utility.setStringPreference(ParentpalActivityLogin.this, Utility.KEY_PASSWORD, "");
			}
			
			Intent intent = new Intent(ParentpalActivityLogin.this, ParentpalTabActivity.class);
			startActivity(intent);
			finish();
					 
		}else {
			
			ShowSimpleDialog("Error", responseMsg);
			
		}
	}
}
