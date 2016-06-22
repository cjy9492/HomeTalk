
package com.cjy.hometalk.ui;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.cjy.hometalk.AppManager;
import com.cjy.hometalk.BaseActivity;
import com.cjy.hometalk.R;
import com.cjy.hometalk.R.id;
import com.cjy.hometalk.R.layout;
import com.cjy.hometalk.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ChatLoginActivity extends BaseActivity {

	private EditText mUsernameET;
	private EditText mPasswordET;
	private TextView mPasswordForgetTV;
	private Button mSigninBtn;
	private TextView mSignupTV;
	private CheckBox mPasswordCB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_login);

		mUsernameET = (EditText) findViewById(R.id.chat_login_username);
		mPasswordET = (EditText) findViewById(R.id.chat_login_password);
		mPasswordForgetTV = (TextView) findViewById(R.id.chat_login_forget_password);
		mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
		mSignupTV = (TextView) findViewById(R.id.chat_login_signup);
		mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);

		if (EMChat.getInstance().isLoggedIn()) {
			Log.d("TAG", "已经登陆过");
			EMGroupManager.getInstance().loadAllGroups();
			EMChatManager.getInstance().loadAllConversations();
			startActivity(new Intent(ChatLoginActivity.this,
					MainActivity.class));
		}

		mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB.setChecked(true);
					//动态设置密码是否可见
					mPasswordET
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					mPasswordCB.setChecked(false);
					mPasswordET
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				}
			}
		});

		mSigninBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String userName = mUsernameET.getText().toString().trim();
				final String password = mPasswordET.getText().toString().trim();

				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(getApplicationContext(), "请输入用户名",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "请输入密码",
							Toast.LENGTH_SHORT).show();
				} else {
					EMChatManager.getInstance().login(userName, password,
							new EMCallBack() {// 回调
								@Override
								public void onSuccess() {
									runOnUiThread(new Runnable() {
										public void run() {
											EMGroupManager.getInstance()
													.loadAllGroups();
											EMChatManager.getInstance()
													.loadAllConversations();
											Log.d("main", "登陆聊天服务器成功！");
											Toast.makeText(
													getApplicationContext(),
													"登陆成功", Toast.LENGTH_SHORT)
													.show();
											startActivity(new Intent(
													ChatLoginActivity.this,
													MainActivity.class));
//											mApplication.mSharedPreferences
//													.edit()
//													.putString("loginName",
//															userName).commit();
										}
									});
								}

								@Override
								public void onProgress(int progress,
										String status) {

								}

								@Override
								public void onError(int code, String message) {
									if (code == -1005) {
										message = "用户名或密码错误";
									}
									final String msg = message;
									runOnUiThread(new Runnable() {
										public void run() {
											Log.d("main", "登陆聊天服务器失败！");
											Toast.makeText(
													getApplicationContext(),
													msg, Toast.LENGTH_SHORT)
													.show();
										}
									});
								}
							});
				}
			}
		});

		mSignupTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ChatLoginActivity.this,
						ChatRegisterActivity.class));
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			new AlertDialog.Builder(ChatLoginActivity.this)
					.setTitle("应用提示")
					.setMessage(
							"确定要退出"
									+ getResources().getString(
											R.string.app_name) + "客户端吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									AppManager.getInstance().AppExit(
											ChatLoginActivity.this);
									System.exit(0);
									ChatLoginActivity.this.finish();
									 
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).show();
		}

		return super.onKeyDown(keyCode, event);
	}
}
