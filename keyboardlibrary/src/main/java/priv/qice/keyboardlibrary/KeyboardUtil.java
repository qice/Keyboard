package priv.qice.keyboardlibrary;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

/**
 * @Author: ys尘笑
 * @date 2015-12-6 下午5:21:36
 * @Description: 键盘功能控制工具类
 */
public class KeyboardUtil {
	private Context ctx;
	private KeyboardView keyboardView;
	private Keyboard k1;// 键盘
//	private Keyboard k2;
//	private Keyboard k3;
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	public final static int TYPE_NUMBER = 1; // 数量
	public final static int TYPE_PRICE = 2; // 价格
	private int type = -1;
	private KeyboardListener keyboardListener;

	private EditText ed;

	public interface KeyboardListener {
		void onOK();
	}

	public KeyboardUtil setEdit(EditText edit){
		ed = edit;
		ed.setFocusable(false);
		ed.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( !isShowing() ) {
					showKeyboard();
				}
				return false;
			}
		});

		return this;
	}

	/**
	 * @param ctx
	 * @param parent	包含MyKeyboardView的ViewGroup
	 * @param edit
	 */
	public KeyboardUtil(Context ctx, View parent, EditText edit) {
		this.ctx = ctx;
		this.ed = edit;
		//此处可替换键盘xml
		k1 = new Keyboard(ctx, R.xml.number);
		keyboardView = (KeyboardView) parent.findViewById(R.id.keyboard_view);
		//keyboardView.setContext(ctx);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	/**
	 * @param ctx	必须要用Activity实例作为上下文传入
	 */
	public KeyboardUtil(Context ctx) {
		this.ctx = ctx;

		initViews();
		LayoutInflater.from(ctx).inflate(R.layout.keyboard, contentContainer);
		decorView.addView(rootView);
		rootView.requestFocus();

		//此处可替换键盘xml
		k1 = new Keyboard(ctx, R.xml.number);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	public void setKeyboardListener(KeyboardListener keyboardListener) {
		this.keyboardListener = keyboardListener;
	}

	public void setType(int typein) {
		type = typein;
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				hideKeyboard();
				//keyboardListener.onOK();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
//					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else if (primaryCode == 46) {	   // 小数点
				String text = ed.getText().toString();
				if (type == TYPE_PRICE) {
					if (!ed.getText().toString().contains(".") && text.length() <= 7) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
				}
			} else {
				String text = ed.getText().toString();
				switch (type) {
				case TYPE_NUMBER:
					if (text.length() < 7) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
					break;

				case TYPE_PRICE:
					if ((!text.contains(".") || text.length() - 1
							- text.indexOf(".") <= 1)
							&& text.length() < (text.contains(".")?10:7)) {
						//小数点后最长2位，接受7位整数
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
					break;
				default:
					editable.insert(start,
							Character.toString((char) primaryCode));
					break;
				}

			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		if ( !isShowing() ) {
			decorView.addView(rootView);
		}
	}

	public void hideKeyboard() {
		if ( isShowing() ) {
			decorView.removeView(rootView);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}



	public ViewGroup decorView;//显示pickerview的根View,默认是activity的根view
	private ViewGroup rootView;//附加View 的 根View
	protected ViewGroup contentContainer;
	private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
	);
	protected void initViews() {
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);

		//如果只是要显示在屏幕的下方
		//decorView是activity的根View
		if (decorView == null) {
			decorView = (ViewGroup) ((Activity) ctx).getWindow().getDecorView().findViewById(android.R.id.content);
		}
		//将控件添加到decorView中
		rootView = (ViewGroup) layoutInflater.inflate(R.layout.keyboard_rootview, decorView, false);
		rootView.setLayoutParams(new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
		));

		//这个是真正要加载时间选取器的父布局
		contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
		contentContainer.setLayoutParams(params);
	}

	public View findViewById(int id) {
		return contentContainer.findViewById(id);
	}

	public boolean isShowing() {
		return rootView.getParent() != null;
	}
}
