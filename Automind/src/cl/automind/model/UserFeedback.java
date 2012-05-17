package cl.automind.model;

public class UserFeedback {
	public enum Result {
		Answered(1),
		NotShown(0),
		Timeout(-1),
		Skipped(-2);
		private int mark;
		Result(int mark){
			setMark(mark);
		}
		private void setMark(int mark){
			if(this != Answered) return;
			this.mark = mark;
		}
		private int getMark(){
			return mark;
		}
	}
	public UserFeedback(){
		result = Result.NotShown;
	}
	public UserFeedback(Result result){
		this.result = result != null ? result : Result.NotShown;
	}
	public UserFeedback(Result result, int mark){
		this(result);
		setMark(mark);
	}
	public UserFeedback(Result result, String message){
		this(result);
		setMessage(message);
	}
	public UserFeedback(Result result, int mark, String message){
		this(result, mark);
		setMessage(message);
	}
	private final Result result;
	private String message;
	public void setMark(int mark){
		getResult().setMark(mark);
	}
	public int getMark(){
		return getResult().getMark();
	}
	public Result getResult() {
		return result;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
}
