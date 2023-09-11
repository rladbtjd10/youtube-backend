@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @Column(name = "REVIEW_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_review")
    @SequenceGenerator(name = "seq_review", sequenceName = "SEQ_REVIEW", allocationSize = 1)
    private int reviewCode;

    @Column(name = "MEM_ID")
    private String memId;

    @Column(name = "REVIEW_CONTENT")
    private String reviewContent;

    @Column(name = "REVIEW_GRADE")
    private int reviewGrade;

    @Column(name = "REVIEW_DATE", columnDefinition = "DATE DEFAULT SYSDATE")
    private Date reviewDate;

    @Column(name = "PARENTS_COMMENT_SEQ")
    private String parentsCommentSeq;

    @Column(name = "MEM_CODE")
    private int memCode;

    @Column(name = "RES_CODE")
    private int resCode;
}