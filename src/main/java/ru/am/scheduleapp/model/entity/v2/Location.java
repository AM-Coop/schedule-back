package ru.am.scheduleapp.model.entity.v2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedule_location")
public class Location {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(unique = true)
    private Integer num;

    @Column(unique = true)
    private String name;

    private String region;
    private String timeZone;
    private String address;
    private String rout;
    private String icon;
    private String description;

    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RegionDict regionDict;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;
}
