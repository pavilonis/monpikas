package lt.pavilonis.cmm.api.rest.user;

import com.google.common.io.BaseEncoding;
import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.school.user.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class UserRepository {

   private static final Logger LOGGER = getLogger(UserRepository.class.getSimpleName());
   private static final String BLOCKS_FROM_WHERE = "" +
         "FROM User " +
         "WHERE " +
         "  cardCode IS NOT NULL " +
         "  AND (:name IS NULL OR name LIKE :name) " +
         "  AND (:role IS NULL OR organizationRole LIKE :role) " +
         "  AND (:group IS NULL OR organizationGroup LIKE :group)" +
         "  AND (:withFirstLastNameOnly = '0' OR (name IS NOT NULL AND name <> ''))";

   private final NamedParameterJdbcTemplate jdbc;

   public UserRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public User update(User user) {

      String base16photo = user.getBase16photo();
      String picture = StringUtils.isNotBlank(base16photo) && BaseEncoding.base16().canDecode(base16photo)
            ? "  Picture = CONVERT(VARBINARY(MAX), :base16photo, 2) "
            : "  Picture = NULL ";

      String sql = "UPDATE User " +
            "SET " +
            "  name = :name, " +
            "  birthDate = :birthDate, " +
            "  organizationGroup = :group, " +
            "  organizationRole = :role," +
            picture +
            "WHERE cardCode = :cardCode";

      jdbc.update(sql, collectArgs(user));
      return load(user.getCardCode(), false);
   }

   public List<User> load(UserFilter filter) {

      Map<String, Object> args = commonArgs(filter);
      args.put("argOffset", QueryUtils.argOffset(filter.getOffset()));
      args.put("argLimit", QueryUtils.argLimit(filter.getLimit()));

      var sql = "SELECT " +
            "  cardCode, " +
            "  name, " +
            "  birthDate, " +
            "  organizationGroup, " +
            "  organizationRole," +
            "  NULL AS photo " + // Photos are only loaded for single user requests
            BLOCKS_FROM_WHERE +
            "ORDER BY name ASC " +
            "LIMIT :argLimit OFFSET :argOffset";

      return jdbc.query(sql, args, new UserMapper());
   }

   protected Map<String, Object> commonArgs(UserFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("name", QueryUtils.likeArg(filter.getName()));
      args.put("role", filter.getRole());
      args.put("group", filter.getGroup());
      args.put("withFirstLastNameOnly", filter.isWithFirstLastNameOnly());
      return args;
   }


   public User load(String cardCode, boolean withPhoto) {
      List<User> result = jdbc.query("" +
                  "SELECT " +
                  "  cardCode, " +
                  "  name, " +
                  "  birthDate, " +
                  "  organizationGroup, " +
                  "  organizationRole," +
                  "  CASE WHEN :withPhoto = 1 " +
                  "     THEN " +
                  "        picture " +
                  "     ELSE " +
                  "        NULL " +
                  "  END AS photo " +
                  "FROM User " +
                  "WHERE cardCode IS NOT NULL AND cardCode = :cardCode ",

            Map.of("cardCode", cardCode, "withPhoto", withPhoto),
            new UserMapper()
      );

      if (result.isEmpty()) {
         LOGGER.warn("User not found - returning NULL [cardCode={}]", cardCode);
         return null;
      } else {
         return result.get(0);
      }
   }

   private Map<String, Object> collectArgs(User user) {

      String photo = StringUtils.isNotBlank(user.getBase16photo()) && BaseEncoding.base16().canDecode(user.getBase16photo())
            ? user.getBase16photo()
            : null;

      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", user.getCardCode());
      args.put("name", user.getName());
      args.put("group", user.getOrganizationGroup());
      args.put("role", user.getOrganizationRole());
      args.put("birthDate", user.getBirthDate());
      args.put("base16photo", photo);
      args.put("now", LocalDateTime.now());
      return args;
   }

   public boolean exists(String cardCode) {
      return jdbc.queryForObject("" +
                  "SELECT " +
                  "  CASE " +
                  "     WHEN COUNT(cardCode) > 0 " +
                  "     THEN 1 " +
                  "     ELSE 0 " +
                  "  END " +
                  "FROM User " +
                  "WHERE cardCode IS NOT NULL AND cardCode = :cardCode",
            Map.of("cardCode", cardCode),
            Boolean.class
      );
   }

   public int size(UserFilter filter) {
      return jdbc.queryForObject(
            "SELECT COUNT(cardCode) " + BLOCKS_FROM_WHERE,
            commonArgs(filter),
            Integer.class
      );
   }

   public List<String> loadGroups() {
      String sql = "SELECT DISTINCT organizationGroup " +
            "FROM User " +
            "WHERE organizationGroup IS NOT NULL " +
            "  AND organizationGroup <> '' " +
            "ORDER BY organizationGroup ASC";
      return jdbc.queryForList(sql, Map.of(), String.class);
   }

   public List<String> loadRoles() {
      var sql = "SELECT DISTINCT organizationRole " +
            "FROM User " +
            "WHERE organizationRole IS NOT NULL " +
            "  AND organizationRole <> '' " +
            "ORDER BY organizationRole ASC";
      return jdbc.queryForList(sql, Map.of(), String.class);
   }
}
