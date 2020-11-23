package com.oamkgroup2.heartbeat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Heartbeat_logs")
public class Log {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO) 
    Long id;
    String content;


    public Log(){}

    Log(long id, String content){
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
