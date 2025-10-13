import com.ChickenKitchen.app.enum.Role
import com.ChickenKitchen.app.model.entity.auth.MailToken
import com.ChickenKitchen.app.model.entity.auth.UserSession
import com.ChickenKitchen.app.model.entity.order.Order
import com.ChickenKitchen.app.model.entity.user.Wallet
import jakarta.persistence.*
import java.sql.Timestamp


@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_username", columnList = "username"),
        Index(name = "idx_email", columnList = "email")
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(nullable = false, length = 100)
    val username: String,

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(name = "is_verified", nullable = false)
    val isVerified: Boolean = false,

    @Column(length = 100)
    val phone: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Timestamp? = null,

    @Column(name = "updated_at")
    var updatedAt: Timestamp? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = false,

    @Column(name = "full_name", nullable = false, length = 100)
    val fullName: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sessions: MutableList<UserSession> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val mailTokens: MutableList<MailToken> = mutableListOf(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val wallet: Wallet? = null,

    @OneToMany(mappedBy = "user")
    val orders: MutableList<Order> = mutableListOf(),
)
