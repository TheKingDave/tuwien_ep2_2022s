public class MyBodyLink {
    private MyBodyLink before;
    private MyBodyLink after;
    private Body body;

    public MyBodyLink getBefore() {
        return before;
    }

    public void setBefore(MyBodyLink before) {
        this.before = before;
    }

    public MyBodyLink getAfter() {
        return after;
    }

    public void setAfter(MyBodyLink after) {
        this.after = after;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
