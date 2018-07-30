package org.javaee8.jaxrs.sseproducer.data;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author daniel
 */
public class EventData {

    private Date time;
    private String id;
    private String comment;

    public EventData() {
    }

    public EventData(String comment) {
        this.setTime(new Date());
        this.setId(UUID.randomUUID().toString());
        this.setComment(comment);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
