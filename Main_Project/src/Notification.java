
public abstract class Notification {
	private String title;
	private String message;
	private User user;

	public Notification(String title, String message, User user) {
		this.title = title;
		this.message = message;
	}

	@Override
	public String toString() {
		return "Notification [title=" + title + ", message=" + message + ", user=" + user + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
