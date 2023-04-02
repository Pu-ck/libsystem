package com.system.libsystem.entities.yearofprint;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class YearOfPrintEntity {

    @Id
    @SequenceGenerator(name = "year_of_print_id_seq", sequenceName = "year_of_print_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "year_of_print_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private int yearOfPrint;

}
