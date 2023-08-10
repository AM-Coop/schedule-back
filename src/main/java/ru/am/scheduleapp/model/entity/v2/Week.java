package ru.am.scheduleapp.model.entity.v2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Week {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    //    @Indexed(unique = true)
    private Integer num;

    @Column(length = 10000)

    private String quote;

    @Column(length = 10000)
    private String notes;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @OneToMany(mappedBy = "week", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Event> eventList = List.of();
}
