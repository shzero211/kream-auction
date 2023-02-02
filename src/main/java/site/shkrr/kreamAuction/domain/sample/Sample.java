package site.shkrr.kreamAuction.domain.sample;


import javax.persistence.*;

@Entity
@Table(name = "tt")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
