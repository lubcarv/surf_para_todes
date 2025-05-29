package dev.surfparatodes.surfparatodes.model.user.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.surfparatodes.surfparatodes.converters.user.SchoolingTypeConverter;
import dev.surfparatodes.surfparatodes.converters.user.TypeUserConverter;
import dev.surfparatodes.surfparatodes.enums.SchoolingType;
import dev.surfparatodes.surfparatodes.enums.TypeRole;
import dev.surfparatodes.surfparatodes.enums.UserRole;
import dev.surfparatodes.surfparatodes.validation.ValidTypeUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Tipo de usuário. Valores válidos: ALUNO (1), PROFESSOR (2).")
    @ValidTypeUser
    @NotNull(message = "O tipo de usuário deve ser declarado e ser Aluno ou Professor.")
    @Convert(converter = TypeUserConverter.class)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @NotNull(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Column(name = "register_name", nullable = false, length = 100)
    private String registerName;

    @Size(min = 3, max = 100, message = "O nome social deve ter entre 3 e 100 caracteres.")
    @Column(name = "social_name", length = 100)
    private String socialName;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull(message = "Forneça o nome de um responsável para contato.")
    @Size(min = 3, max = 100, message = "O nome do responsável deve ter entre 3 e 100 caracteres.")
    @Column(name = "guardian_name", length = 100)
    private String guardianName;

    @NotNull(message = "Forneça o nível de parentesco do responsável.")
    @Size(min = 3, max = 50, message = "O nível de parentesco deve ter entre 3 e 50 caracteres.")
    @Column(name = "guardian_relationship", length = 50)
    private String guardianRelationship;

    @NotNull(message = "Forneça um contato de um responsável.")
    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Forneça um número válido para o responsável.")
    @Column(name = "guardian_phone", length = 15)
    private String guardianPhone;

    @Pattern(regexp = "^(Masculino|Feminino|Outro)$", message = "Gênero inválido. Valores permitidos: Masculino, Feminino ou Outro.")
    @Column(name = "gender", length = 20)
    private String gender;

    @Pattern(regexp = "^(Branco|Negro|Pardo|Indígena|Outro)$", message = "Raça inválida. Valores permitidos: Branco, Negro, Pardo, Indígena ou Outro.")
    @Column(name = "race", length = 20)
    private String race;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Formato de telefone inválido")
    @Column(name = "phone", length = 15)
    private String phone;

    @Email(message = "Formato de email inválido")
    @Column(name = "email", length = 50)
    private String email;

    @NotNull(message = "Informe o grau de escolaridade.")
    @Convert(converter = SchoolingTypeConverter.class)
    @Column(name = "schooling", nullable = false)
    private SchoolingType schooling;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Schema(hidden = true)
    @JsonIgnore
    public String getDisplayName() {
        return (socialName != null && !socialName.isBlank())
                ? socialName.trim()
                : (registerName != null ? registerName.trim() : "");
    }
}
