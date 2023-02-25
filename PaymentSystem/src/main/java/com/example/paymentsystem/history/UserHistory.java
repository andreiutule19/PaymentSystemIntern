package com.example.paymentsystem.history;

import com.example.paymentsystem.abstraction.AbstractUsers;
import com.example.paymentsystem.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserHistory extends AbstractUsers implements Comparable<UserHistory> {

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private Status lastStatus;
    private String executor;
    private Timestamp timestamp;
    private String username;

    public UserHistory(String email, String password, String fullName, String address,
                       Status currentStatus, Status lastStatus,String executor,String username){
        super(email,password,fullName,address,currentStatus);
        this.lastStatus=lastStatus;
        this.executor=executor;
        this.username=username;
        this.timestamp=Timestamp.from(Instant.now());

    }

    @Override
    public int compareTo(UserHistory o) {
        return o.getUsername().compareTo(this.username);
    }
}
