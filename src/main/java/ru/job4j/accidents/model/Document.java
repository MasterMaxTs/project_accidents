package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных Сопроводительный документ
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "documents")
public class Document {

    /**
     * Идентификатор документа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя сопроводительного документа
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Сопроводительный документ
     */
    private byte[] data;

    /**
     * Автор документа
     */
    private String author;

    /**
     * Автоинцидент
     */
    @ManyToOne()
    @JoinColumn(name = "accident_id")
    private Accident accident;
}
