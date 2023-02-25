package com.example.paymentsystem.abstraction;

import com.example.paymentsystem.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractUsers {
    @Id
    @Column(name ="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private String fullName;
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private Status currentStatus;


    public AbstractUsers( String email, String password, String fullName, String address, Status currentStatus) {
        this.email=email;
        this.password=password;
        this.fullName=fullName;
        this.address=address;
        this.currentStatus = currentStatus;
    }


}
