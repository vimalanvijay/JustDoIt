package com.example.todo;

public class todo {

    private String text;
    private String id;
    private boolean done;

    public todo(){

    }

    public todo(String id_in, String text_in,boolean doneStatus){
        this.id=id_in;
        this.text=text_in;
        this.done=doneStatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean d) {
        this.done = d;
    }
}
