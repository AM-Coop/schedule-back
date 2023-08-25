package ru.am.scheduleapp.model.entity.v2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class WeekInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(length = 10000)

    private String quote;

    @Column(length = 10000)
    private String note1;
    @Column(length = 10000)
    private String note2;

    //    @Column(length = 10000)
    private String community;

    @ManyToOne
    private Week week;


}
