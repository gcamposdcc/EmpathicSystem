package cl.automind.empathy.fwdroid.ui;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cl.automind.empathy.android.R;
import cl.automind.empathy.feedback.AbstractMessage;
import cl.automind.empathy.fw.ui.AbstractUiManager;

public abstract class AbstractAndroidUiManager extends AbstractUiManager{
	private final int askUser = 25;
	private final Random randomGen = new Random();
	@Override
	public void displayMessage(AbstractMessage message) {
		displayMessage(message, getActivity());
	}
	public void displayMessage(AbstractMessage message, Activity activity) {
		if (getRandomGen().nextInt(100) < getAskUser()){
			showEmpathicDialog(activity, message);
		} else {
			showEmpathicToast(activity, message);
		}
	}
	public abstract Activity getActivity();
	@Override
	public void hideCurrentMessage() {
		// TODO Auto-generated method stub

	}

	protected abstract String getUrl();

	protected AbstractMessage.Context duplicateContext(AbstractMessage.Context context){
		return context.duplicate();
	}

	protected void showEmpathicToast(Activity activity, AbstractMessage message){
		final AbstractMessage.Context context = duplicateContext(message.getContext());
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.empathic_toast, (ViewGroup) activity.findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.emp_toast_image);
		image.setImageResource(R.drawable.icon);
		TextView text = (TextView) layout.findViewById(R.id.emp_toast_text);
		text.setText(message.getText());

		final Toast toast = new Toast(activity.getApplicationContext());
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
		new CountDownTimer(9000, 1000) {
		    public void onTick(long millisUntilFinished) {toast.show();}
		    public void onFinish() {toast.show();}
		}.start();
		registerMessageOcurrence(context, false, false, false);
	}

	protected void showEmpathicDialog(Activity activity, AbstractMessage message){
		final AbstractMessage.Context context = duplicateContext(message.getContext());
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.empathic_dialog,
		                               (ViewGroup) activity.findViewById(R.id.emp_dialog_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.emp_dialog_text);
		text.setText(message.getText());
		ImageView image = (ImageView) layout.findViewById(R.id.emp_dialog_image);
		image.setImageResource(R.drawable.icon);

		builder = new AlertDialog.Builder(activity);
		builder = builder.setView(layout);
		builder = builder.setCancelable(false)
			       .setPositiveButton("Me gusta", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   registerMessageOcurrence(context, true, true, true);
			        	   dialog.cancel();
			           }
			       })
			       .setNegativeButton("No me gusta", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   registerMessageOcurrence(context, true, true, false);
			        	   dialog.cancel();
			           }
			       });
		alertDialog = builder.create();

		alertDialog.show();
	}
	private int getAskUser() {
		return askUser;
	}

	private Random getRandomGen() {
		return randomGen;
	}
}
